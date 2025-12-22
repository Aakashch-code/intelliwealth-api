package com.example.intelliwealth.repository;

import com.example.intelliwealth.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByType(String type);

    List<Transaction> findByDescriptionContainingIgnoreCase(String keyword);
}
