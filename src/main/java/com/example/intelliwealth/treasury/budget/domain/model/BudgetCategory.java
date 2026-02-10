package com.example.intelliwealth.treasury.budget.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BudgetCategory {

    GROCERIES("Groceries"),
    RENT("Rent"),
    UTILITIES("Utilities"),
    TRANSPORT("Transport"),
    HEALTHCARE("Healthcare"),
    INSURANCE("Insurance"),

    ENTERTAINMENT("Entertainment"),
    DINING("Dining"),
    SHOPPING("Shopping"),
    TRAVEL("Travel"),
    FITNESS("Fitness"),
    PERSONAL_CARE("Personal Care"),

    SAVINGS("Savings"),
    INVESTMENTS("Investments"),
    LOANS("Loans"),
    CREDIT_CARD("Credit Card"),
    TAXES("Taxes"),

    SUBSCRIPTIONS("Subscriptions"),
    INTERNET("Internet"),
    MOBILE_RECHARGE("Mobile Recharge"),

    EDUCATION("Education"),
    CHILDCARE("Childcare"),
    GIFTS("Gifts"),
    DONATIONS("Donations"),

    OFFICE_EXPENSES("Office Expenses"),
    BUSINESS("Business"),

    EMERGENCY("Emergency"),
    MISCELLANEOUS("Miscellaneous");

    private final String label;



}
