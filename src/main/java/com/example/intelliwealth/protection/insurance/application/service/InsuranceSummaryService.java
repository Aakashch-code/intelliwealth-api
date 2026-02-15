package com.example.intelliwealth.protection.insurance.application.service;

import com.example.intelliwealth.protection.insurance.application.dto.InsuranceCategorySummary;
import com.example.intelliwealth.protection.insurance.application.dto.InsuranceResponseDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
public class InsuranceSummaryService {

    public InsuranceCategorySummary summarize(List<InsuranceResponseDTO> policies) {

        if (policies == null || policies.isEmpty()) {
            return InsuranceCategorySummary.builder()
                    .policyCount(0)
                    .daysToNextRenewal(-1)
                    .renewalStatus("NO_ACTIVE_POLICIES")
                    .build();
        }

        // Financial totals
        BigDecimal totalCoverage = policies.stream()
                .map(InsuranceResponseDTO::getCoverageAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPremium = policies.stream()
                .map(InsuranceResponseDTO::getPremiumAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<String> names = policies.stream()
                .map(InsuranceResponseDTO::getName)
                .toList();

        // Date calculation
        LocalDate today = LocalDate.now();

        LocalDate nextRenewal = policies.stream()
                .map(InsuranceResponseDTO::getEndDate)
                .filter(date -> date.isAfter(today))
                .min(Comparator.naturalOrder())
                .orElse(null);

        long daysGap = -1;
        String status = "UNKNOWN";

        if (nextRenewal != null) {
            daysGap = ChronoUnit.DAYS.between(today, nextRenewal);

            if (daysGap <= 30) status = "URGENT_RENEWAL_NEEDED";
            else if (daysGap <= 90) status = "UPCOMING_RENEWAL";
            else status = "SAFE";
        }

        return InsuranceCategorySummary.builder()
                .policyCount(policies.size())
                .totalCoverageAmount(totalCoverage)
                .totalAnnualPremium(totalPremium)
                .policyNames(names)
                .nextRenewalDate(nextRenewal)
                .daysToNextRenewal(daysGap)
                .renewalStatus(status)
                .build();
    }
}
