package com.example.intelliwealth.wealth.asset;

import java.util.List;
import java.util.Map;

public class AssetValidator {

    public static void validateAttributes(
            AssetCategory category,
            Map<String, Object> attributes
    ) {
        List<String> requiredFields =
                AssetAttributeRules.REQUIRED_FIELDS.get(category);

        if (requiredFields == null) return;

        for (String field : requiredFields) {
            if (!attributes.containsKey(field)) {
                throw new IllegalArgumentException(
                        "Missing required attribute: " + field + " for category: " + category
                );
            }
        }
    }
}
