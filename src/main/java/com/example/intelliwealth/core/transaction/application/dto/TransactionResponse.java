package com.example.intelliwealth.core.transaction.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Transaction response details")
public class TransactionResponse {
    private String type;
    private String description;
    private BigDecimal amount;
    private String category;
    private String source;
    private LocalDate transactionDate;
}