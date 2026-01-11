package com.example.intelliwealth.core.budget;

import com.example.intelliwealth.authentication.security.SecuredService;
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

    // ---------------- CREATE ----------------

    public BudgetResponseDTO createBudget(BudgetRequestDTO request) {
        Budget budget = mapper.toEntity(request);
        budget.setUserId(currentUserId()); // 🔐 attach owner

        return mapper.toResponseDTO(repo.save(budget));
    }

    // ---------------- READ ----------------

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

    // ---------------- UPDATE ----------------

    public BudgetResponseDTO updateBudget(int id, BudgetRequestDTO request) {
        Budget existing = repo.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new BudgetNotFoundException("Budget not found"));

        mapper.updateEntityFromRequest(request, existing);
        return mapper.toResponseDTO(repo.save(existing));
    }

    // ---------------- DELETE ----------------

    public void deleteBudgetById(int id) {
        Budget budget = repo.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new BudgetNotFoundException("Budget not found"));

        repo.delete(budget);
    }

    // ---------------- SUMMARY ----------------

    public BudgetSummaryDTO getBudgetSummary() {
        List<Budget> budgets = repo.findAllByUserId(currentUserId());

        BigDecimal totalAllocated = budgets.stream()
                .map(Budget::getAmountAllocated)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSpent = budgets.stream()
                .map(Budget::getAmountSpent)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return BudgetSummaryDTO.builder()
                .totalAllocated(totalAllocated)
                .totalSpent(totalSpent)
                .build();
    }
}
