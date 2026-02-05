package com.example.intelliwealth.wealth.asset.domain.rules;

import com.example.intelliwealth.wealth.asset.domain.model.AssetCategory;

import java.util.List;
import java.util.Map;

public class AssetAttributeRules {

    public static final Map<AssetCategory, List<String>> REQUIRED_FIELDS = Map.of(

            AssetCategory.REAL_ESTATE, List.of("location", "areaSqFt", "propertyType", "status"),

            AssetCategory.EQUITY, List.of("tickerSymbol", "exchange", "quantity", "avgBuyPrice"),


            AssetCategory.MUTUAL_FUND, List.of("fundHouse", "schemeName", "folioNumber", "units", "nav"),

            AssetCategory.FIXED_INCOME, List.of("issuer", "interestRate", "maturityDate", "accountNumber"),

            AssetCategory.CRYPTO, List.of("coinSymbol", "walletAddress", "network", "quantity"),

            AssetCategory.VEHICLE, List.of("registrationNo", "modelYear", "insuranceExpiry"),

            AssetCategory.CASH, List.of("bankName", "accountType")
    );
}