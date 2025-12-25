package com.example.intelliwealth.dto.goal;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "Response object representing a saved goal")
public class GoalResponse {

    @Schema(description = "Unique ID of the goal", example = "1")
    private long id;

    @Schema(description = "Name of the goal", example = "Buy a Tesla")
    private String name;

    @Schema(description = "Target amount", example = "50000.00")
    private BigDecimal targetAmount;

    @Schema(description = "Current saved amount", example = "1500.00")
    private BigDecimal currentAmount;

    @Schema(description = "Amount spent from this goal", example = "0.00")
    private BigDecimal spentAmount;

    @Schema(description = "Priority level", example = "HIGH")
    private String priority;

    @Schema(description = "Target date", example = "25-12-2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date targetDate;
}