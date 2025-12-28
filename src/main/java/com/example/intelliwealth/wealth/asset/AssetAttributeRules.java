package com.example.intelliwealth.wealth.asset;

import java.util.List;
import java.util.Map;

public class AssetAttributeRules {

    public static final Map<AssetCategory, List<String>> REQUIRED_FIELDS =
            Map.of(
                    AssetCategory.REAL_ESTATE, List.of("areaSqFt", "location"),
                    AssetCategory.MUTUAL_FUND, List.of("fundHouse", "nav"),
                    AssetCategory.VEHICLE, List.of("registrationNo"),
                    AssetCategory.CRYPTO, List.of("wallet", "chain")
            );
}
