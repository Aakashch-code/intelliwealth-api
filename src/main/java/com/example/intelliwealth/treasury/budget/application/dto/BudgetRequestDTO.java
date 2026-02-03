package com.example.intelliwealth.treasury.budget.application.dto;

import com.example.intelliwealth.treasury.budget.domain.model.BudgetCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Object used to create or update a budget")
public class BudgetRequestDTO {

    @NotNull(message = "Title is required")
    @Schema(description = "The title of budget" , example = "Protein Supplement")
    private String title;

    @NotNull(message = "Category is required")
    @Schema(description = "The category of the budget", example = "GROCERIES")
    private BudgetCategory category;

    @NotNull(message = "Start date is required")
    @Schema(description = "Start date of the budget period", example = "2023-12-01")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Schema(description = "End date of the budget period", example = "2023-12-31")
    private LocalDate endDate;

    @NotNull(message = "Allocated amount is required")
    @Positive(message = "Allocated amount must be positive")
    @Schema(description = "Total amount allocated", example = "500.00")
    private BigDecimal amountAllocated;

    @PositiveOrZero(message = "Amount spent cannot be negative")
    @Schema(description = "Amount currently spent (default 0)", example = "0.00")
    private BigDecimal amountSpent = BigDecimal.ZERO;

    @Schema(description = "Is this a recurring budget?", example = "true")
    private boolean recurring;

    @Schema(description = "Optional notes", example = "Holiday shopping included")
    private String note;
}