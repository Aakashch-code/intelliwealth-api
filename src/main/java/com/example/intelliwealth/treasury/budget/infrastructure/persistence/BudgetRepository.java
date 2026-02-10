package com.example.intelliwealth.treasury.budget.infrastructure.persistence;

import com.example.intelliwealth.treasury.budget.application.dto.BudgetSummaryDTO;
import com.example.intelliwealth.treasury.budget.domain.model.Budget;
import com.example.intelliwealth.treasury.transaction.domain.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Page<Budget> findAllByUserId(UUID userId, Pageable pageable);

    Optional<Budget> findByIdAndUserId(Long id, UUID userId);

    @Query("SELECT (" +
            "COALESCE(SUM(b.amountAllocated), 0), " +
            "COALESCE(SUM(b.amountSpent), 0), " +
            "COALESCE(SUM(b.amountAllocated) - SUM(b.amountSpent), 0)) " +
            "FROM Budget b WHERE b.userId = :userId")
    BudgetSummaryDTO findBudgetSummaryByUserId(@Param("userId") UUID userId);



}
