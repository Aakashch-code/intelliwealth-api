package com.example.intelliwealth.treasury.budget.application.dto;

import com.example.intelliwealth.config.CurrencySerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Summary object containing total calculations across all budgets")
public class BudgetSummaryDTO {

    @Schema(description = "Sum of all allocated amounts across all budgets", example = "2500.00")
    @JsonSerialize(using = CurrencySerializer.class)
    private BigDecimal totalAllocated;

    @Schema(description = "Sum of all spent amounts across all budgets", example = "1200.50")
    @JsonSerialize(using = CurrencySerializer.class)
    private BigDecimal totalSpent;
}