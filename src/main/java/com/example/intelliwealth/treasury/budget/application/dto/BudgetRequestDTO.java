package com.example.intelliwealth.treasury.budget.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "Object used to create or update a budget")
public class BudgetRequestDTO {

    @Schema(description = "The category of the budget", example = "Groceries")
    private String category;

    @Schema(description = "The billing date for the budget", example = "2023-12-01")
    private Date billingPeriod;

    @Schema(description = "Total amount allocated for this category", example = "500.00")
    private BigDecimal amountAllocated;

    @Schema(description = "Amount currently spent", example = "150.75")
    private BigDecimal amountSpent;
}