package com.example.intelliwealth.core.budget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository repo;

    @Autowired
    private BudgetMapper mapper;

    public List<BudgetResponseDTO> getAllBudgets() {
        return mapper.toResponseDTOList(repo.findAll());
    }

    public BudgetResponseDTO getBudgetById(int id) {
        return repo.findById(id)
                .map(mapper::toResponseDTO)
                .orElse(null); // Or throw ResourceNotFoundException
    }

    public BudgetResponseDTO createBudget(BudgetRequestDTO request) {
        Budget budget = mapper.toEntity(request);
        return mapper.toResponseDTO(repo.save(budget));
    }

    public BudgetResponseDTO updateBudget(int id, BudgetRequestDTO request) {
        Budget existingBudget = repo.findById(id).orElse(null);
        if (existingBudget != null) {
            mapper.updateEntityFromRequest(request, existingBudget);
            return mapper.toResponseDTO(repo.save(existingBudget));
        }
        return null;
    }

    public void deleteBudgetById(int id) {
        repo.deleteById(id);
    }

    // Consolidated Method: Calculates both totals in one go
    public BudgetSummaryDTO getBudgetSummary() {
        List<Budget> allBudgets = repo.findAll();

        BigDecimal totalAllocated = allBudgets.stream()
                .map(Budget::getAmountAllocated)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSpent = allBudgets.stream()
                .map(Budget::getAmountSpent)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return BudgetSummaryDTO.builder()
                .totalAllocated(totalAllocated)
                .totalSpent(totalSpent)
                .build();
    }
}