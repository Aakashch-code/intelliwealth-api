package com.example.intelliwealth.treasury.transaction.application.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavingResponse {

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal saving;
}
