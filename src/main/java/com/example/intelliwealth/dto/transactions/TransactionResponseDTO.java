package com.example.intelliwealth.dto.transactions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Transaction response payload")
public class TransactionResponseDTO {

    @Schema(example = "TXN-2025-001")
    private String transactionId;

    @Schema(example = "EXPENSE")
    private String type;

    @Schema(example = "Grocery shopping at D-Mart")
    private String description;

    @Schema(example = "1250.50")
    private BigDecimal amount;

    @Schema(example = "INR")
    private String currency;

    @Schema(example = "Food")
    private String category;

    @Schema(example = "UPI")
    private String source;

    @Schema(example = "2025-01-15")
    private LocalDate createdAt;
}
