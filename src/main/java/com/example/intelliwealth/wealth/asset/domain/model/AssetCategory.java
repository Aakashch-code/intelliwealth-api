package com.example.intelliwealth.wealth.asset.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AssetCategory {
    BANK_ACCOUNT("Bank Account"),
    REAL_ESTATE("Real Estate"),
    EQUITY("Equity"),
    MUTUAL_FUND("Mutual Fund"),
    FIXED_INCOME("Fixed Income"),
    CRYPTO("Crypto"),
    VEHICLE("Vehicle"),
    CASH("Cash"),
    OTHER("Other");

    private final String label;
}
