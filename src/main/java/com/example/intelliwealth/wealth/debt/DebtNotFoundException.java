package com.example.intelliwealth.wealth.debt;


public class DebtNotFoundException extends RuntimeException {

    public DebtNotFoundException(String id) {
        super("Debt not found with id: " + id);
    }
}

