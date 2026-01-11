package com.example.intelliwealth.core.budget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BudgetRepository extends JpaRepository<Budget , Integer> {
    List<Budget> findAllByUserId(UUID userId);
    Optional<Budget> findByIdAndUserId(int id, UUID userId);
}
