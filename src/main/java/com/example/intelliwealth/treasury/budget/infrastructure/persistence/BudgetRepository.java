package com.example.intelliwealth.treasury.budget.infrastructure.persistence;

import com.example.intelliwealth.treasury.budget.domain.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    List<Budget> findAllByUserId(UUID userId);
    Optional<Budget> findByIdAndUserId(int id, UUID userId);
}