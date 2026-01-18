package com.example.intelliwealth.treasury.budget.application.dto;

import com.example.intelliwealth.config.CurrencySerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "Response object containing budget details")
public class BudgetResponseDTO {

    @Schema(description = "Unique identifier of the budget", example = "1")
    private int id;

    @Schema(description = "The category of the budget", example = "Groceries")
    private String category;

    @Schema(description = "The billing date formatted as dd-MM-yyyy", example = "01-12-2023")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date billingPeriod;

    @Schema(description = "Total amount allocated", example = "500.00")
    @JsonSerialize(using = CurrencySerializer.class)
    private BigDecimal amountAllocated;

    @Schema(description = "Total amount spent", example = "150.75")
    @JsonSerialize(using = CurrencySerializer.class)
    private BigDecimal amountSpent;
}