package com.example.intelliwealth.wealth.asset.validation;

import com.example.intelliwealth.wealth.asset.domain.AssetCategory;

import java.util.List;
import java.util.Map;

public class AssetAttributeRules {

    public static final Map<AssetCategory, List<String>> REQUIRED_FIELDS = Map.of(
            // 1. REAL ESTATE
            AssetCategory.REAL_ESTATE, List.of("location", "areaSqFt", "propertyType", "status"),

            // 2. EQUITY
            AssetCategory.EQUITY, List.of("tickerSymbol", "exchange", "quantity", "avgBuyPrice"),

            // 3. MUTUAL FUND
            AssetCategory.MUTUAL_FUND, List.of("fundHouse", "schemeName", "folioNumber", "units", "nav"),

            // 4. FIXED INCOME
            AssetCategory.FIXED_INCOME, List.of("issuer", "interestRate", "maturityDate", "accountNumber"),

            // 5. CRYPTO (This fixes your error)
            // Old rule was: List.of("wallet", "chain")
            // New rule matches your JSON:
            AssetCategory.CRYPTO, List.of("coinSymbol", "walletAddress", "network", "quantity"),
            // 6. VEHICLE
            AssetCategory.VEHICLE, List.of("registrationNo", "modelYear", "insuranceExpiry"),

            // 7. CASH
            AssetCategory.CASH, List.of("bankName", "accountType")
    );
}