package com.example.intelliwealth.treasury.budget.application.service;

import com.example.intelliwealth.authentication.application.service.SecuredService;
import com.example.intelliwealth.treasury.budget.application.dto.*;
import com.example.intelliwealth.treasury.budget.domain.exception.BudgetNotFoundException;
import com.example.intelliwealth.treasury.budget.domain.model.*;
import com.example.intelliwealth.treasury.budget.infrastructure.mapper.BudgetMapper;
import com.example.intelliwealth.treasury.budget.infrastructure.persistence.BudgetRepository;
import com.example.intelliwealth.treasury.transaction.application.service.TransactionService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("isAuthenticated()")
public class BudgetService extends SecuredService {

    private final BudgetRepository repo;
    private final BudgetMapper mapper;

    @Autowired
    @Lazy
    private TransactionService transactionService;

    // -------- CRUD --------

    @CacheEvict(value = "budget_summary", key = "#root.target.cacheKey()")
    public BudgetResponseDTO createBudget(BudgetRequestDTO request) {

        Budget budget = mapper.toEntity(request);
        budget.setUserId(currentUserId());
        budget.setMode(BudgetMode.ACTIVE);

        Budget saved = repo.save(budget);

        if (isPositive(saved.getAmountSpent())) {
            transactionService.createSystemExpense(
                    saved.getId(),
                    null,
                    saved.getAmountSpent(),
                    saved.getTitle() + " (Initial Spent)",
                    saved.getNote()
            );
        }

        return mapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public Page<BudgetResponseDTO> getBudgets(Pageable pageable) {

        if (pageable.getPageSize() > 100) {
            throw new IllegalArgumentException("Page size too large");
        }

        return repo.findAllByUserId(currentUserId(), pageable)
                .map(mapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public BudgetResponseDTO getBudgetById(Long id) {
        return mapper.toResponseDTO(getBudget(id));
    }


    @CacheEvict(value = "budget_summary", key = "#root.target.cacheKey()")
    public BudgetResponseDTO updateBudget(Long id, BudgetRequestDTO request) {

        Budget budget = getBudget(id);

        BigDecimal oldSpent = safe(budget.getAmountSpent());

        mapper.updateEntityFromRequest(budget, request);
        Budget updated = repo.save(budget);

        BigDecimal newSpent = safe(updated.getAmountSpent());

        if (newSpent.compareTo(oldSpent) > 0) {

            BigDecimal diff = newSpent.subtract(oldSpent);

            transactionService.createSystemExpense(
                    updated.getId(),
                    null,
                    diff,
                    updated.getTitle() + " (Manual Adjustment)",
                    updated.getNote()
            );
        }

        return mapper.toResponseDTO(updated);
    }


    @CacheEvict(value = "budget_summary", key = "#root.target.cacheKey()")
    public void deleteBudget(Long id) {

        Budget budget = getBudget(id);

        transactionService.deleteTransactionsByBudgetId(id);

        repo.delete(budget);
    }

    // -------- SPENDING --------
    @CacheEvict(value = "budget_summary", key = "#root.target.cacheKey()")
    public void addSpentAmount(Long budgetId, AddExpenseRequest request) {

        Budget budget = getBudget(budgetId);

        budget.addSpentAmount(request);

        Budget saved = repo.save(budget);

        if (isPositive(request.amount())) {
            transactionService.createSystemExpense(
                    saved.getId(),
                    null,
                    request.amount(),
                    request.title(),
                    saved.getNote()
            );
        }
    }

    @Transactional(readOnly = true)
    public void validateBudgetIsActive(Long budgetId) {

        Budget budget = getBudget(budgetId);

        if (budget.getMode() == BudgetMode.SUSPENDED) {
            throw new IllegalStateException(
                    "Transaction rejected: Budget is SUSPENDED."
            );
        }
    }

    // -------- SUMMARY --------

    @Transactional(readOnly = true)
    @Cacheable(value = "budget_summary", key = "#root.target.cacheKey()", unless = "#result == null")
    public BudgetSummaryDTO getBudgetSummary() {

        return repo.findBudgetSummaryByUserIdAndMode(
                currentUserId(),
                BudgetMode.ACTIVE
        );
    }

    // -------- MAINTENANCE --------

    public void deleteAllBudgets() {
        repo.deleteAll();
    }

    // -------- HELPERS --------

    private Budget getBudget(Long id) {

        return repo.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() ->
                        new BudgetNotFoundException("Budget with ID " + id + " not found"));
    }

    private BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }
}