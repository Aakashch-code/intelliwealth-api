package com.example.intelliwealth.fynix.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FynixRequest {

    @NotBlank
    private String query;

    private boolean includeBudgets = true;
    private boolean includeSubscriptions = true;
    private boolean includeTransactions = true;
    private boolean includeGoals = true;
}

