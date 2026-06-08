package com.example.intelliwealth.wealth.debt.application.service;

import com.example.intelliwealth.authentication.application.service.SecuredService;
import com.example.intelliwealth.wealth.debt.application.dto.DebtStatsDTO;
import com.example.intelliwealth.wealth.debt.domain.model.DebtStatus;
import com.example.intelliwealth.wealth.debt.domain.rules.DebtValidator;
import com.example.intelliwealth.wealth.debt.domain.model.Debt;
import com.example.intelliwealth.wealth.debt.application.dto.DebtRequestDTO;
import com.example.intelliwealth.wealth.debt.application.dto.DebtResponseDTO;
import com.example.intelliwealth.wealth.debt.domain.exception.DebtNotFoundException;
import com.example.intelliwealth.wealth.debt.infrastructure.mapper.DebtMapper;
import com.example.intelliwealth.wealth.debt.infrastructure.persistence.DebtRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.Decimal128;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class DebtService extends SecuredService {

    private final DebtRepository repo;
    private final DebtMapper mapper;

    public List<DebtResponseDTO> getAll() {
        return repo.findAllByUserId(currentUserId())
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public DebtResponseDTO getById(String id) {
        Debt debt = repo.findById(id)
                .orElseThrow(() -> new DebtNotFoundException(id));

        if (!debt.getUserId().equals(currentUserId())) {
            throw new AccessDeniedException("Access denied");
        }

        return mapper.toDto(debt);
    }

    @Caching(evict = {
            @CacheEvict(value = "debt_stats", key = "#root.target.cacheKey()"),
            @CacheEvict(value = "upcoming_debt", key = "#root.target.cacheKey()")
    })
     public DebtResponseDTO create(DebtRequestDTO dto) {

        if (dto.getAttributes() == null) {
            dto.setAttributes(new HashMap<>());
        }
        if (dto.getDueDate() == null) {
            dto.setDueDate(LocalDate.now().plusMonths(1));
        }
        DebtValidator.validate(dto.getCategory(), dto.getAttributes());

        Debt debt = mapper.toEntity(dto);
        debt.setUserId(currentUserId());
        repo.findAllByUserId(currentUserId());


        return mapper.toDto(repo.save(debt));
    }
    @Caching(evict = {
            @CacheEvict(value = "debt_stats", key = "#root.target.cacheKey()"),
            @CacheEvict(value = "upcoming_debt", key = "#root.target.cacheKey()")
    })
    public DebtResponseDTO update(String id, DebtRequestDTO dto) {

        Debt existing = repo.findById(id)
                .orElseThrow(() -> new DebtNotFoundException(id));

        if (!existing.getUserId().equals(currentUserId())) {
            throw new AccessDeniedException("Access denied");
        }

        DebtValidator.validate(dto.getCategory(), dto.getAttributes());

        existing.setName(dto.getName());
        existing.setCreditor(dto.getCreditor());
        existing.setTotalAmount(dto.getTotalAmount());
        existing.setOutstandingAmount(dto.getOutstandingAmount());
        existing.setCategory(dto.getCategory());
        existing.setMainCategory(dto.getMainCategory());
        existing.setAttributes(dto.getAttributes());
        existing.setDueDate(dto.getDueDate());

        return mapper.toDto(repo.save(existing));
    }
    @Caching(evict = {
            @CacheEvict(value = "debt_stats", key = "#root.target.cacheKey()"),
            @CacheEvict(value = "upcoming_debt", key = "#root.target.cacheKey()")
    })
    public void delete(String id) {
        repo.deleteByIdAndUserId(id,currentUserId());
    }

    public BigDecimal totalOutstandingAmount() {
        return repo.findAllByUserId(currentUserId())
                .stream()
                .map(d -> d.getOutstandingAmount() == null
                        ? BigDecimal.ZERO
                        : d.getOutstandingAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Decimal128 totalDebtAmount() {
        return repo.sumOfTotalDebtByUserId(currentUserId());
    }

    @Cacheable(value = "debt_stats", key = "#root.target.cacheKey()")
    public DebtStatsDTO debtAmountSummary() {
        return DebtStatsDTO.builder()
                .totalDebtAmount(totalDebtAmount())
                .totalOutstandingAmount(totalOutstandingAmount())
                .build();
    }

    @Cacheable(value = "upcoming_debt", key = "#root.target.cacheKey()")
    public Map<String, BigDecimal> getNextFiveMonthsEMIs() {
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        LocalDate now = LocalDate.now();

        for (int i = 0; i < 5; i++) {
            LocalDate date = now.plusMonths(i);
            String monthKey = date.getMonth().name() + "-" + date.getYear();
            result.put(monthKey, BigDecimal.ZERO);
        }

        List<Debt> activeDebts = repo.findAllByUserIdAndStatus(currentUserId(), DebtStatus.ACTIVE);

        for (Debt debt : activeDebts) {
            Map<String, Object> attrs = debt.getAttributes();

            if (attrs == null || !attrs.containsKey("emiAmount") || !attrs.containsKey("remainingTenureMonths")) {
                continue;
            }

            BigDecimal emiAmount = extractAmount(attrs, "emiAmount");

            int remainingTenure;
            try {
                remainingTenure = Integer.parseInt(attrs.get("remainingTenureMonths").toString());
            } catch (Exception e) {
                continue;
            }

            LocalDate simulatedDate = debt.getDueDate() != null ? debt.getDueDate() : now;

            for (int i = 0; i < remainingTenure; i++) {
                String expectedMonthKey = simulatedDate.getMonth().name() + "-" + simulatedDate.getYear();

                if (result.containsKey(expectedMonthKey)) {
                    BigDecimal currentTotal = result.get(expectedMonthKey);
                    result.put(expectedMonthKey, currentTotal.add(emiAmount));
                }

                simulatedDate = simulatedDate.plusMonths(1);

                if (simulatedDate.isAfter(now.plusMonths(5))) {
                    break;
                }
            }
        }

        return result;
    }

    public BigDecimal getTotalMonthlyEMIs() {
        List<Debt> debts = repo.findAllByUserId(currentUserId());
        BigDecimal total = BigDecimal.ZERO;

        for (Debt debt : debts) {
            if (debt.getStatus() == DebtStatus.PAID) continue;

            Map<String, Object> attrs = debt.getAttributes();
            if (attrs == null) continue;

            BigDecimal monthly = switch (debt.getCategory()) {
                case HOME_LOAN, PERSONAL_LOAN, CAR_LOAN, EMI -> extractAmount(attrs, "emiAmount");
                case CREDIT_CARD -> extractAmount(attrs, "minPayment");
                case FRIEND_LOAN -> extractAmount(attrs, "repaymentAmount");
            };

            if (monthly != null) {
                total = total.add(monthly);
            }
        }
        return total;
    }

    private BigDecimal extractAmount(Map<String, Object> attributes, String key) {
        Object val = attributes.get(key);
        if (val == null) return BigDecimal.ZERO;

        try {
            return new BigDecimal(val.toString());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

}
