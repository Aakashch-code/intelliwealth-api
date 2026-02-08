package com.example.intelliwealth.treasury.budget.application.service;

import com.example.intelliwealth.authentication.application.service.SecuredService;
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
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("isAuthenticated()")
public class BudgetService extends SecuredService {

    private final BudgetRepository repo;
    private final BudgetMapper mapper;

    public BudgetResponseDTO createBudget(BudgetRequestDTO request) {
        Budget budget = mapper.toEntity(request);
        budget.setUserId(currentUserId());
        return mapper.toResponseDTO(repo.save(budget));
    }

    @Transactional(readOnly = true)
    public List<BudgetResponseDTO> getAllBudgets() {
        return mapper.toResponseDTOList(
                repo.findAllByUserId(currentUserId())
        );
    }

    @Transactional(readOnly = true)
    public BudgetResponseDTO getBudgetById(Long id) {
        return repo.findByIdAndUserId(id, currentUserId())
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new BudgetNotFoundException("Budget with ID " + id + " not found"));
    }

    public BudgetResponseDTO updateBudget(Long id, BudgetRequestDTO request) {
        Budget existing = repo.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new BudgetNotFoundException("Budget with ID " + id + " not found"));

        mapper.updateEntityFromRequest(existing, request);
        return mapper.toResponseDTO(repo.save(existing));
    }

    public void deleteBudgetById(Long id) {
        Budget budget = repo.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new BudgetNotFoundException("Budget with ID " + id + " not found"));

        repo.delete(budget);
    }


    @Transactional(readOnly = true)
    public BudgetSummaryDTO getBudgetSummary() {

        // find current user
        UUID userId = currentUserId();

        // calculated from DB
        BigDecimal totalAllocated = repo.sumAllocatedByUserId(userId);
        BigDecimal totalSpent = repo.sumSpentAmountByUserId(userId);

        // remaining
        BigDecimal totalRemaining = totalAllocated.subtract(totalSpent);

        return BudgetSummaryDTO.builder()
                .totalAllocated(totalAllocated)
                .totalSpent(totalSpent)
                .totalRemaining(totalRemaining)
                .build();
    }


}