package com.example.intelliwealth.treasury.budget.application.service;

import com.example.intelliwealth.authentication.application.SecuredService;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetRequestDTO;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetResponseDTO;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetSummaryDTO;
import com.example.intelliwealth.treasury.budget.application.mapper.BudgetMapper;
import com.example.intelliwealth.treasury.budget.domain.exception.BudgetNotFoundException;
import com.example.intelliwealth.treasury.budget.domain.model.Budget;
import com.example.intelliwealth.treasury.budget.infrastructure.persistence.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("isAuthenticated()")
public class BudgetService extends SecuredService {

    private final BudgetRepository repo;
    private final BudgetMapper mapper;

    public BudgetResponseDTO createBudget(BudgetRequestDTO request) {
        Budget budget = mapper.toEntity(request);
        budget.setUserId(currentUserId()); // 🔐 attach owner

        return mapper.toResponseDTO(repo.save(budget));
    }

    public List<BudgetResponseDTO> getAllBudgets() {
        return mapper.toResponseDTOList(
                repo.findAllByUserId(currentUserId())
        );
    }

    public BudgetResponseDTO getBudgetById(int id) {
        return repo.findByIdAndUserId(id, currentUserId())
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new BudgetNotFoundException("Budget not found"));
    }

    public BudgetResponseDTO updateBudget(int id, BudgetRequestDTO request) {
        Budget existing = repo.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new BudgetNotFoundException("Budget not found"));

        mapper.updateEntityFromRequest(existing, request);
        return mapper.toResponseDTO(repo.save(existing));
    }

    public void deleteBudgetById(int id) {
        Budget budget = repo.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new BudgetNotFoundException("Budget not found"));

        repo.delete(budget);
    }

    public BudgetSummaryDTO getBudgetSummary() {
        List<Budget> budgets = repo.findAllByUserId(currentUserId());

        BigDecimal totalAllocated = budgets.stream()
                .map(b -> b.getAmountAllocated() != null ? b.getAmountAllocated() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSpent = budgets.stream()
                .map(b -> b.getAmountSpent() != null ? b.getAmountSpent() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return BudgetSummaryDTO.builder()
                .totalAllocated(totalAllocated)
                .totalSpent(totalSpent)
                .build();
    }
}