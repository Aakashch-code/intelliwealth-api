package com.example.intelliwealth.protection.insurance.validation;

import java.util.Set;

public class InsuranceAttributeRules {

    public static Set<String> HEALTH = Set.of(
            "hospitalNetwork", "policyType", "roomRentLimit"
    );

    public static Set<String> VEHICLE = Set.of(
            "vehicleNumber", "vehicleType", "policyType"
    );

    public static Set<String> LIFE = Set.of(
            "nominee", "maturityDate"
    );
}
