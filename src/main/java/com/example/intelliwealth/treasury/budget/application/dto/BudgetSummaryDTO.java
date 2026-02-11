package com.example.intelliwealth.treasury.budget.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BudgetSummaryDTO {
    private BigDecimal totalAllocated;
    private BigDecimal totalSpent;
    private BigDecimal totalRemaining;


}