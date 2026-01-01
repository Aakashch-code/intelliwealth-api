package com.example.intelliwealth.core.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Transaction request payload")
public class TransactionRequestDTO {

    @Schema(
            description = "Transaction type",
            example = "EXPENSE",
            allowableValues = {"INCOME", "EXPENSE"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String type;

    @Schema(
            description = "Short description",
            example = "Grocery shopping at D-Mart"
    )
    private String description;

    @Schema(
            description = "Transaction amount",
            example = "1250.50",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private BigDecimal amount;

    @Schema(
            description = "Currency code",
            example = "INR",
            defaultValue = "INR"
    )
    private String currency = "INR";

    @Schema(description = "Transaction category", example = "Food")
    private String category;
    @Schema(description = "Transaction date", example = "2025-12-25")
    private LocalDate transactionDate;

    @Schema(description = "Source of transaction", example = "UPI / Bank / Cash")
    private String source;
}
