package com.example.intelliwealth.wealth.debt.application.dto;

import java.math.BigDecimal;

public class DebtSumProjection {
    private BigDecimal totalValue;

    public BigDecimal getTotalDebt() {
        return totalValue;
    }

    public void setTotalDebt(BigDecimal totalDebt) {
        this.totalValue = totalDebt;
    }
}