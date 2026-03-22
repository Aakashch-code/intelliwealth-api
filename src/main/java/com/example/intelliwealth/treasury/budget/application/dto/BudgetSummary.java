package com.example.intelliwealth.treasury.budget.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BudgetSummary {
    private BigDecimal totalAllocated;
    private BigDecimal totalSpent;
    private BigDecimal totalRemaining;

    public BudgetSummary(BigDecimal totalAllocated, BigDecimal totalSpent, BigDecimal totalRemaining) {
        this.totalAllocated = totalAllocated;
        this.totalSpent = totalSpent;
        this.totalRemaining = totalRemaining;
    }
}