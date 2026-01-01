package com.example.intelliwealth.protection.contingency;

import java.math.BigDecimal;

public record ContingencyReportDTO(
        BigDecimal totalMonthlyBurn,      // (Avg Expense + Subs + EMI)
        BigDecimal totalLiquidAssets,     // (Cash + Savings + Liquid Funds)
        BigDecimal monthsOfRunway,        // How many months you can survive
        BigDecimal recommendedGap,        // How much more you need to save for 6 months safety
        String status                     // "CRITICAL", "AT RISK", "SAFE"
) {}