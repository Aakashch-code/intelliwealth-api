package com.example.intelliwealth.treasury.budget.application.dto;

import com.example.intelliwealth.treasury.budget.domain.model.BudgetCategory;
import com.example.intelliwealth.treasury.budget.domain.model.BudgetMode;
import com.example.intelliwealth.treasury.budget.domain.model.BudgetStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Response object containing budget details")
public class BudgetResponseDTO {

    private Long id;
    private String title;
    private BudgetCategory category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private BigDecimal amountAllocated;
    private BigDecimal amountSpent;
    private BigDecimal remainingAmount;
    private BudgetStatus status;
    private BudgetMode mode;

    private boolean recurring;
    private String note;
}