package com.example.intelliwealth.wealth.debt.domain.rules;

import com.example.intelliwealth.wealth.debt.domain.model.DebtCategory;
import java.util.List;
import java.util.Map;

public class DebtAttributeRules {

    public static final Map<DebtCategory, List<String>> REQUIRED_FIELDS =
            Map.of(
                    DebtCategory.CREDIT_CARD, List.of("interestRate", "minPayment"),
                    DebtCategory.HOME_LOAN, List.of("interestRate", "emiAmount", "totalTenureMonths", "remainingTenureMonths"),
                    DebtCategory.PERSONAL_LOAN, List.of("interestRate", "emiAmount", "totalTenureMonths", "remainingTenureMonths"),
                    DebtCategory.CAR_LOAN, List.of("interestRate", "emiAmount", "totalTenureMonths", "remainingTenureMonths"),
                    DebtCategory.EMI, List.of("emiAmount", "totalTenureMonths", "remainingTenureMonths")
            );
}