package com.example.intelliwealth.treasury.subscription.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum BillingCycle {

    DAILY,
    WEEKLY,
    MONTHLY,
    QUARTERLY,
    ANNUAL;

    public BigDecimal calculateMonthly(BigDecimal amount) {

        return switch (this) {

            case DAILY ->
                    amount.multiply(new BigDecimal("30"));

            case WEEKLY ->
                    amount.multiply(new BigDecimal("4.33"));

            case MONTHLY ->
                    amount;

            case QUARTERLY ->
                    amount.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);

            case ANNUAL ->
                    amount.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
        };
    }
}
