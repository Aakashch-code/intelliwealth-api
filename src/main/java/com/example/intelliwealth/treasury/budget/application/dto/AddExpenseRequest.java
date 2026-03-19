package com.example.intelliwealth.treasury.budget.application.dto;

import java.math.BigDecimal;

public record AddExpenseRequest(String title, BigDecimal amount) {
}
