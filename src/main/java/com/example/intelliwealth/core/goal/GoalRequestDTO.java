package com.example.intelliwealth.core.goal;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "Request object for creating or updating a goal")
public class GoalRequestDTO {

    @Schema(description = "Name of the financial goal", example = "Buy a Tesla")
    private String name;

    @Schema(description = "The target amount to reach", example = "50000.00")
    private BigDecimal targetAmount;

    @Schema(description = "Initial amount already saved (optional)", example = "1000.00")
    private BigDecimal currentAmount;

    @Schema(description = "Priority level of the goal", example = "HIGH")
    private String priority;

    @Schema(description = "Target completion date (dd-MM-yyyy)", example = "25-12-2025", type = "string", pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date targetDate;
}