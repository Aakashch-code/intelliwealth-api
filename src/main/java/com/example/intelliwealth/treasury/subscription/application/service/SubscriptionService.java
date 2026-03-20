package com.example.intelliwealth.treasury.subscription.application.service;

import com.example.intelliwealth.authentication.application.service.SecuredService;
import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionRequestDTO;
import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionResponseDTO;
import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionStatDTO;
import com.example.intelliwealth.treasury.subscription.domain.model.BillingCycle;
import com.example.intelliwealth.treasury.subscription.infrastrcture.mapper.SubscriptionMapper;
import com.example.intelliwealth.treasury.subscription.domain.exception.SubscriptionNotFoundException;
import com.example.intelliwealth.treasury.subscription.domain.model.Subscription;
import com.example.intelliwealth.treasury.subscription.infrastrcture.persistence.SubscriptionRepository;
import com.example.intelliwealth.treasury.transaction.application.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("isAuthenticated()")
public class SubscriptionService extends SecuredService {

    private final SubscriptionRepository repo;
    private final SubscriptionMapper mapper;
    private final TransactionService transactionService;
    // ---------------- READ ----------------

    @Transactional(readOnly = true)
    public Page<SubscriptionResponseDTO> getAllSubscriptions(Pageable pageable) {
        if(pageable.getPageSize()>100) {
            throw new IllegalArgumentException("Page Size Is Too Large");
        }
        return repo.findAllByUserId(currentUserId(), pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<SubscriptionResponseDTO> getActiveSubscriptions( Pageable pageable) {
        return repo.findByUserIdAndIsActiveTrue(currentUserId(),pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<SubscriptionResponseDTO> getInactiveSubscriptions(Pageable pageable) {
        return repo.findByUserIdAndIsActiveFalse(currentUserId(),pageable).map(mapper::toResponse);

    }

    @Transactional(readOnly = true)
    public SubscriptionResponseDTO getSubscriptionById(Long id) {
        return repo.findByIdAndUserId(id, currentUserId())
                .map(mapper::toResponse)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));
    }

    // ---------------- CREATE ----------------
    @Caching(evict = {
            @CacheEvict(value = "monthly_subscription", key = "#root.target.cacheKey()"),
            @CacheEvict(value = "subscription_stats", key = "#root.target.cacheKey()")
    })
    public SubscriptionResponseDTO createSubscription(SubscriptionRequestDTO dto) {
        Subscription entity = mapper.toEntity(dto);
        entity.setUserId(currentUserId());

        // 1. Set the subscription as active and set the first billing date to today
        entity.setActive(true);
        LocalDate today = LocalDate.now();

        // 2. Calculate when the NEXT charge should happen
        entity.setNextBillingDate(calculateNextBillingDate(today, entity.getBillingCycle()));

        // 3. Save the subscription
        Subscription saved = repo.save(entity);

        // 4. Log the INITIAL expense immediately into the transactions table
        transactionService.createSystemExpense(
                null,
                saved.getId(),
                saved.getAmount(),
                saved.getTitle(),
                "Initial subscription charge"
        );

        return mapper.toResponse(saved);
    }
    //helper
    private LocalDate calculateNextBillingDate(LocalDate currentDate, BillingCycle cycle) {
        return switch (cycle) {
            case WEEKLY -> currentDate.plusWeeks(1);
            case MONTHLY -> currentDate.plusMonths(1);
            case QUARTERLY -> currentDate.plusMonths(3);
            case ANNUAL -> currentDate.plusYears(1);
            default -> currentDate.plusMonths(1); // Safe fallback
        };
    }

    // ---------------- UPDATE ----------------


    @Caching(evict = {
            @CacheEvict(value = "monthly_subscription", key = "#root.target.cacheKey()"),
            @CacheEvict(value = "subscription_stats", key = "#root.target.cacheKey()")
    })
    public SubscriptionResponseDTO toggleSubscriptionStatus(Long id) {
        Subscription sub = repo.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));

        sub.setActive(!sub.isActive());
        return mapper.toResponse(repo.save(sub));
    }

    // ---------------- DELETE ----------------

    @Caching(evict = {
            @CacheEvict(value = "monthly_subscription", key = "#root.target.cacheKey()"),
            @CacheEvict(value = "subscription_stats", key = "#root.target.cacheKey()")
    })
    public void hardDeleteSubscription(Long id) {
        Subscription sub = repo.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));

        // 1. Wipe the transaction history (because it was a typo or full refund)
        transactionService.deleteTransactionsBySubscriptionId(sub.getId());

        // 2. Wipe the subscription
        repo.delete(sub);
    }

    // ---------------- AGGREGATE ----------------

    @Transactional(readOnly = true)
    @Cacheable(value = "monthly_subscription" , key = "#root.target.cacheKey()")
    public BigDecimal getTotalMonthlySubscriptions() {

        return repo.calculateTotalByUserIdAndCycle(
                currentUserId(),
                BillingCycle.MONTHLY
        );
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "subscription_stats", key = "#root.target.cacheKey()")
    public SubscriptionStatDTO getStats() {

        List<Object[]> rows = repo.sumByCycle(currentUserId());

        BigDecimal monthlyTotal = BigDecimal.ZERO;

        for (Object[] row : rows) {

            BillingCycle cycle = (BillingCycle) row[0];
            BigDecimal amount  = (BigDecimal) row[1];

            // Convert each cycle → monthly
            BigDecimal monthly = cycle.calculateMonthly(amount);

            monthlyTotal = monthlyTotal.add(monthly);
        }

        // Now derive others from monthly
        BigDecimal daily = monthlyTotal
                .divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP);

        BigDecimal weekly = monthlyTotal
                .divide(new BigDecimal("4.33"), 2, RoundingMode.HALF_UP);

        BigDecimal quarterly = monthlyTotal
                .multiply(new BigDecimal("3"))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal yearly = monthlyTotal
                .multiply(new BigDecimal("12"))
                .setScale(2, RoundingMode.HALF_UP);


        return new SubscriptionStatDTO(
                daily,
                weekly,
                monthlyTotal,
                quarterly,
                yearly
        );
    }

}
