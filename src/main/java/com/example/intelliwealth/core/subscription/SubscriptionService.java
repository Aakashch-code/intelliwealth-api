package com.example.intelliwealth.core.subscription;

import com.example.intelliwealth.authentication.security.SecuredService;
import com.example.intelliwealth.core.subscription.SubscriptionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("isAuthenticated()")
public class SubscriptionService extends SecuredService {

    private final SubscriptionRepository repo;
    private final SubscriptionMapper mapper;

    // ---------------- READ ----------------

    public List<SubscriptionResponseDTO> getAllSubscriptions() {
        return repo.findAllByUserId(currentUserId())
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<SubscriptionResponseDTO> getActiveSubscriptions() {
        return repo.findByUserIdAndIsActiveTrue(currentUserId())
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<SubscriptionResponseDTO> getInactiveSubscriptions() {
        return repo.findByUserIdAndIsActiveFalse(currentUserId())
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public SubscriptionResponseDTO getSubscriptionById(Long id) {
        return repo.findByIdAndUserId(id, currentUserId())
                .map(mapper::toResponse)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));
    }

    // ---------------- CREATE ----------------

    public SubscriptionResponseDTO createSubscription(SubscriptionRequestDTO dto) {
        Subscription entity = mapper.toEntity(dto);
        entity.setUserId(currentUserId()); // 🔐 owner

        return mapper.toResponse(repo.save(entity));
    }

    // ---------------- UPDATE ----------------

    public SubscriptionResponseDTO toggleSubscriptionStatus(Long id) {
        Subscription sub = repo.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));

        sub.setActive(!sub.isActive());
        return mapper.toResponse(repo.save(sub));
    }

    // ---------------- DELETE ----------------

    public void hardDeleteSubscription(Long id) {
        Subscription sub = repo.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));

        repo.delete(sub);
    }

    // ---------------- AGGREGATE ----------------

    public BigDecimal getTotalMonthlySubscriptions() {
        List<Subscription> activeSubs =
                repo.findByUserIdAndIsActiveTrue(currentUserId());

        BigDecimal totalMonthly = BigDecimal.ZERO;

        for (Subscription sub : activeSubs) {
            BigDecimal amount = sub.getAmount();
            if (amount == null) continue;

            String cycle = sub.getBillingCycle().toUpperCase();

            if (cycle.contains("YEAR") || cycle.contains("ANNUAL")) {
                totalMonthly = totalMonthly.add(
                        amount.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP)
                );
            } else if (cycle.contains("QUARTER")) {
                totalMonthly = totalMonthly.add(
                        amount.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP)
                );
            } else if (cycle.contains("WEEK")) {
                totalMonthly = totalMonthly.add(
                        amount.multiply(BigDecimal.valueOf(4.33))
                );
            } else {
                // MONTHLY
                totalMonthly = totalMonthly.add(amount);
            }
        }
        return totalMonthly;
    }
}
