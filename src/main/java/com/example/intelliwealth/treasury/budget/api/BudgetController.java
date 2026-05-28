package com.example.intelliwealth.treasury.budget.api;

import com.example.intelliwealth.treasury.budget.application.dto.AddExpenseRequest;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetRequest;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetResponse;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetSummary;
import com.example.intelliwealth.treasury.budget.application.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budget")
@RequiredArgsConstructor
@Tag(name = "Budget Controller", description = "Management APIs for Budgeting System")
public class BudgetController {

    private final BudgetService service;

    @Operation(summary = "Get all budgets")
    @GetMapping
    public Page<BudgetResponse> getAllBudgets(Pageable pageable) {

        return service.getBudgets(pageable);
    }

    @Operation(summary = "Get budget by ID")
    @GetMapping("/{id}")
    public BudgetResponse getBudgetById(@PathVariable Long id) {

        return service.getBudgetById(id);
    }

    @Operation(summary = "Get budget summary")
    @GetMapping("/summary")
    public BudgetSummary getBudgetSummary() {

        return service.getBudgetSummary();
    }

    @Operation(summary = "Create budget")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetResponse createBudget(
            @Valid @RequestBody BudgetRequest request) {

        return service.createBudget(request);
    }

    @Operation(summary = "Update budget")
    @PutMapping("/{id}")
    public BudgetResponse updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest request) {

        return service.updateBudget(id, request);
    }

    @Operation(summary = "Delete budget")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBudgetById(@PathVariable Long id) {

        service.deleteBudget(id);
    }

    @Operation(summary = "Delete all budgets")
    @DeleteMapping("/delete-all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllBudget() {

        service.deleteAllBudgets();
    }

    @Operation(summary = "Add spent amount")
    @PutMapping("/spent/{id}")
    public void addSpentAmount(
            @PathVariable Long id,
            @RequestBody AddExpenseRequest request) {

        service.addSpentAmount(id, request);
    }
}