package com.example.intelliwealth.treasury.budget.infrastructure.persistence;

import com.example.intelliwealth.treasury.budget.application.dto.BudgetSummary;
import com.example.intelliwealth.treasury.budget.domain.model.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Page<Budget> findAllByUserId(UUID userId, Pageable pageable);

    Optional<Budget> findByIdAndUserId(Long id, UUID userId);

    @Query("""
    SELECT new com.example.intelliwealth.treasury.budget.application.dto.BudgetSummary(
        COALESCE(SUM(b.amountAllocated), CAST(0 AS bigdecimal)),
        COALESCE(SUM(b.amountSpent), CAST(0 AS bigdecimal)),
        COALESCE(SUM(b.amountAllocated), CAST(0 AS bigdecimal)) 
            - COALESCE(SUM(b.amountSpent), CAST(0 AS bigdecimal))
    )
    FROM Budget b
    WHERE b.userId = :userId AND b.mode = :mode
    """)
    BudgetSummary findBudgetSummaryByUserIdAndMode(@Param("userId") UUID userId, @Param("mode") com.example.intelliwealth.treasury.budget.domain.model.BudgetMode mode);


}
