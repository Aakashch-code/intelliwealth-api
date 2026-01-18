package com.example.intelliwealth.treasury.transaction.domain.validation;

import com.example.intelliwealth.treasury.transaction.application.dto.TransactionRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionDomainValidator {

    public void validate(TransactionRequest request) {
        validateAmount(request.getAmount());
        // Add other complex domain rules here (e.g. strict category checks)
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be strictly positive.");
        }
    }
}