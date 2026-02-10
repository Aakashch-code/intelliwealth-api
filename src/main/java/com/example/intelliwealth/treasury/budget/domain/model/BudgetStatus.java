package com.example.intelliwealth.treasury.budget.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BudgetStatus {
    SAFE("Safe"),
    WARNING("Warning"),
    EXCEEDED("Exceed");
    private final String label;
}
