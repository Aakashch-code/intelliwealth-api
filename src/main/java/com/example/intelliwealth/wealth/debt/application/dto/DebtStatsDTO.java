package com.example.intelliwealth.wealth.debt.application.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class DebtStatsDTO {

    private BigDecimal totalDebtAmount;
    private BigDecimal totalOutstandingAmount;
}
