package com.example.intelliwealth.treasury.transaction.application.dto;

import com.example.intelliwealth.treasury.transaction.domain.model.TransactionCategory;
import com.example.intelliwealth.treasury.transaction.domain.model.TransactionSource;
import com.example.intelliwealth.treasury.transaction.domain.model.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequest {

    @NotNull(message = "Type is required")
    @Schema(example = "EXPENSE")
    private TransactionType type;

    @NotBlank(message = "Description required")
    @Size(max = 255)
    @Schema(example = "Grocery shopping")
    private String description;

    @NotNull(message = "Amount required")
    @Positive(message = "Must be positive")
    @Schema(example = "1200.50")
    private BigDecimal amount;

    @Schema(description = "Transaction category")
    private TransactionCategory category;

    @Schema(example = "UPI")
    private TransactionSource source;

    @NotNull(message = "Date required")
    private LocalDate transactionDate;
}