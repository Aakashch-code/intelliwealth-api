package com.example.intelliwealth.core.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByType(String type);

    List<Transaction> findByDescriptionContainingIgnoreCase(String keyword);
    List<Transaction> findByTypeAndTransactionDateAfter(
            String type,
            LocalDate transactionDate
    );
}
