package com.example.intelliwealth.treasury.transaction.domain.validation;

import com.example.intelliwealth.treasury.transaction.domain.model.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class TransactionDomainValidator {

    public void validate(Transaction transaction) {
        // Business Rule 1: Amount logic (redundant via DTO but good for double safety in domain)
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be strictly positive.");
        }

        // Business Rule 2: Example of a real domain rule (No future transactions allowed)
        if (transaction.getTransactionDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Transaction date cannot be in the future.");
        }
    }
}