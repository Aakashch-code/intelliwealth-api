package com.example.intelliwealth.treasury.budget.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
public class BudgetSummaryDTO {
    private BigDecimal totalAllocated;
    private BigDecimal totalSpent;
    private BigDecimal totalRemaining;

    public BudgetSummaryDTO(
            BigDecimal totalAllocated,
            BigDecimal totalSpent,
            BigDecimal totalRemaining
    ) {
        this.totalAllocated = totalAllocated;
        this.totalSpent = totalSpent;
        this.totalRemaining = totalRemaining;
    }
}