package com.example.intelliwealth.wealth.debt.validation;

import com.example.intelliwealth.wealth.debt.domain.DebtCategory;

import java.util.List;
import java.util.Map;

public class DebtAttributeRules {

    public static final Map<DebtCategory, List<String>> REQUIRED_FIELDS =
            Map.of(
                    DebtCategory.CREDIT_CARD, List.of("interestRate", "minPayment"),
                    DebtCategory.HOME_LOAN, List.of("interestRate", "tenure"),
                    DebtCategory.PERSONAL_LOAN, List.of("interestRate", "emi"),
                    DebtCategory.EMI, List.of("emiAmount", "duration")
            );
}
