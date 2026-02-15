package com.example.intelliwealth.protection.contingency;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
@JsonInclude(JsonInclude.Include.NON_NULL)

public record ContingencyReportDTO(
        BigDecimal totalMonthlyBurn,
        BigDecimal totalLiquidAssets,
        BigDecimal monthsOfRunway,
        BigDecimal recommendedGap,
        String status
) {}