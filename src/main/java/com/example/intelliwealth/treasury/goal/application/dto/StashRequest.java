package com.example.intelliwealth.treasury.goal.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record StashRequest(@NotNull
                           @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
                           BigDecimal amount) {
}
