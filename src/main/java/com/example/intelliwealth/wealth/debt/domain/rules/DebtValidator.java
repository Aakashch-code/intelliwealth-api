package com.example.intelliwealth.wealth.debt.domain.rules;

import com.example.intelliwealth.wealth.debt.domain.model.DebtCategory;

import java.util.Map;

public class DebtValidator {

    public static void validate(DebtCategory category,
                                Map<String, Object> attributes) {

        if (category == null) {
            throw new IllegalArgumentException("Debt category is required");
        }

        var requiredFields =
                DebtAttributeRules.REQUIRED_FIELDS.get(category);

        if (requiredFields == null) return;

        for (String field : requiredFields) {
            if (!attributes.containsKey(field)) {
                throw new IllegalArgumentException(
                        "Missing required attribute: " + field + " for " + category
                );
            }
        }
    }
}
