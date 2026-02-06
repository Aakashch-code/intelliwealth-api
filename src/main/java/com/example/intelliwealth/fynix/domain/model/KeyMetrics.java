package com.example.intelliwealth.fynix.domain.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class KeyMetrics {

    private BigDecimal totalMonthlyIncome;

    private BigDecimal totalMonthlySpend;

    private BigDecimal monthlySubscriptionCost;

    private BigDecimal savingsRate; // percentage
}
