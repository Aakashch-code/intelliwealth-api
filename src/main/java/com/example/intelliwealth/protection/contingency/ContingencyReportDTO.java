package com.example.intelliwealth.protection.contingency;

import java.math.BigDecimal;

public record ContingencyReportDTO(
        BigDecimal totalMonthlyBurn,
        BigDecimal totalLiquidAssets,
        BigDecimal monthsOfRunway,
        BigDecimal recommendedGap,
        String status
) {}