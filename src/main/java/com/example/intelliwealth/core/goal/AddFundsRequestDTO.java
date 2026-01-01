package com.example.intelliwealth.core.goal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Request object for adding funds to an existing goal")
public class AddFundsRequestDTO {

    @Schema(description = "Amount to add to the goal", example = "500.00")
    private BigDecimal amount;
}