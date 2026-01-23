package com.example.intelliwealth.wealth.debt.application.dto;

import com.example.intelliwealth.wealth.debt.domain.model.DebtCategory;
import com.example.intelliwealth.wealth.debt.domain.model.DebtMainCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

@Data
@Schema(description = "Request DTO for creating or updating a debt")
public class DebtRequestDTO {

    @Schema(example = "Home Loan")
    private String name;

    @Schema(example = "HDFC Bank")
    private String creditor;

    @Schema(example = "500000")
    private BigDecimal totalAmount;

    @Schema(example = "350000")
    private BigDecimal outstandingAmount;

    @Schema(example = "LOAN")
    private DebtCategory category;

    @Schema(example = "LONG_TERM")
    private DebtMainCategory mainCategory;

    @Schema(
            description = "Additional dynamic fields based on debt type",
            example = "{\"interestRate\": 8.5, \"tenure\": \"20 years\"}"
    )
    private Map<String, Object> attributes;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(example = "2026-12-23")
    private LocalDate dueDate;
}
