package com.example.intelliwealth.dto.transactions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Transaction data transfer object")
public class TransactionDTO {

    @Schema(
            description = "Transaction type",
            example = "INCOME",
            allowableValues = {"INCOME", "EXPENSE"},
            required = true
    )
    private String type;

    @Schema(
            description = "Short description of the transaction",
            example = "Grocery shopping at D-Mart"
    )
    private String description;

    @Schema(
            description = "Transaction amount",
            example = "1250.50",
            required = true
    )
    private BigDecimal amount;

    @Schema(
            description = "Currency code",
            example = "INR",
            defaultValue = "INR"
    )
    private String currency = "INR";

    @Schema(
            description = "Unique transaction identifier",
            example = "TXN-2025-001"
    )
    private String transactionId;

    @Schema(
            description = "Transaction category",
            example = "Food"
    )
    private String category;

    @Schema(
            description = "Source of transaction",
            example = "UPI / Bank / Cash"
    )
    private String source;

    @Schema(
            description = "Transaction creation date",
            example = "2025-01-15"
    )
    private LocalDate createdAt;
}
