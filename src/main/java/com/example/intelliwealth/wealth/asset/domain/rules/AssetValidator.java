package com.example.intelliwealth.wealth.asset.domain.rules;

import com.example.intelliwealth.wealth.asset.domain.model.AssetCategory;

import java.util.List;
import java.util.Map;

public class AssetValidator {

    private static final Map<AssetCategory, List<String>> REQUIRED_FIELDS = Map.of(
            AssetCategory.REAL_ESTATE, List.of("areaSqFt", "location"),
            AssetCategory.MUTUAL_FUND, List.of("fundHouse", "nav"),
            AssetCategory.VEHICLE, List.of("registrationNo"),
            AssetCategory.CRYPTO, List.of("wallet", "chain")
    );

    public static void validateAttributes(AssetCategory category, Map<String, Object> attributes) {
        // If category has no specific rules, return
        if (!REQUIRED_FIELDS.containsKey(category)) return;

        List<String> required = REQUIRED_FIELDS.get(category);

        if (attributes == null || attributes.isEmpty()) {
            throw new IllegalArgumentException("Category " + category + " requires attributes: " + required);
        }

        for (String field : required) {
            if (!attributes.containsKey(field) || attributes.get(field) == null) {
                throw new IllegalArgumentException(
                        "Missing required attribute: '" + field + "' for category: " + category
                );
            }
        }
    }
}