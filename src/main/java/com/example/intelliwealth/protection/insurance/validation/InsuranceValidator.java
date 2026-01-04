package com.example.intelliwealth.protection.insurance.validation;

import com.example.intelliwealth.protection.insurance.domain.InsuranceCategory;

import java.util.Map;
import java.util.Set;

public class InsuranceValidator {

    public static void validateAttributes(
            InsuranceCategory category,
            Map<String, Object> attributes
    ) {
        Set<String> allowedKeys = switch (category) {
            case HEALTH -> InsuranceAttributeRules.HEALTH;
            case VEHICLE -> InsuranceAttributeRules.VEHICLE;
            case LIFE -> InsuranceAttributeRules.LIFE;
            default -> Set.of();
        };

        if (!allowedKeys.containsAll(attributes.keySet())) {
            throw new IllegalArgumentException("Invalid attributes for " + category);
        }
    }
}
