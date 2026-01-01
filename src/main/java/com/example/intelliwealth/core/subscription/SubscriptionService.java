package com.example.intelliwealth.core.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository repo;
    private final SubscriptionMapper mapper;

    // --- Read Operations ---

    public List<SubscriptionResponseDTO> getAllSubscriptions() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }

    public List<SubscriptionResponseDTO> getActiveSubscriptions() {
        return repo.findByIsActiveTrue().stream().map(mapper::toResponse).toList();
    }

    public List<SubscriptionResponseDTO> getInactiveSubscriptions() {
        return repo.findByIsActiveFalse().stream().map(mapper::toResponse).toList();
    }

    public SubscriptionResponseDTO getSubscriptionById(Long id) {
        return repo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + id));
    }

    // --- Write Operations ---

    public SubscriptionResponseDTO createSubscription(SubscriptionRequestDTO dto) {
        Subscription saved = repo.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    @Transactional
    public SubscriptionResponseDTO toggleSubscriptionStatus(Long id) {
        Subscription sub = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + id));

        sub.setActive(!sub.isActive());
        return mapper.toResponse(sub);
    }

    public void hardDeleteSubscription(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Subscription not found: " + id);
        }
        repo.deleteById(id);
    }

    public BigDecimal getTotalMonthlySubscriptions() {
        List<Subscription> activeSubs = repo.findByIsActiveTrue();
        BigDecimal totalMonthly = BigDecimal.ZERO;

        for (Subscription sub : activeSubs) {
            BigDecimal amount = sub.getAmount();
            String cycle = sub.getBillingCycle().toUpperCase(); // Normalize text

            if (amount == null) continue;

            // Normalize to Monthly cost
            if (cycle.contains("YEAR") || cycle.contains("ANNUAL")) {
                // Divide by 12 (Use RoundingMode to avoid infinite decimals)
                totalMonthly = totalMonthly.add(amount.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP));
            } else if (cycle.contains("QUARTER")) {
                // Divide by 3
                totalMonthly = totalMonthly.add(amount.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP));
            } else if (cycle.contains("WEEK")) {
                // Multiply by 4.33 (average weeks in a month)
                totalMonthly = totalMonthly.add(amount.multiply(new BigDecimal("4.33")));
            } else {
                // Assume MONTHLY
                totalMonthly = totalMonthly.add(amount);
            }
        }
        return totalMonthly;
    }
}
