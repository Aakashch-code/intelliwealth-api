package com.example.intelliwealth.core.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findAllByUserId(UUID userId);

    Optional<Transaction> findByIdAndUserId(int id, UUID userId);

    List<Transaction> findByUserIdAndDescriptionContainingIgnoreCase(
            UUID userId, String keyword
    );

    List<Transaction> findByUserIdAndType(UUID userId, String type);

    void deleteAllByUserId(UUID userId);

    List<Transaction> findByUserIdAndTypeAndTransactionDateAfter(
            UUID userId, String type, LocalDate date
    );
}
