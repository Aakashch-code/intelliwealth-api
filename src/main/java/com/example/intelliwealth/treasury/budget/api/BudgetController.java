package com.example.intelliwealth.treasury.budget.api;

import com.example.intelliwealth.treasury.budget.application.dto.BudgetRequestDTO;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetResponseDTO;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetSummaryDTO;
import com.example.intelliwealth.treasury.budget.application.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/budget")
@Tag(name = "Budget Controller", description = "Management APIs for Budgeting System")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService service;

    @Operation(summary = "Get all budgets", description = "Retrieve a list of all budget entries for the current user.")
    @GetMapping
    public Page<BudgetResponseDTO> getAllBudgets(Pageable pageable) {
        return service.getBudgets(pageable);
    }

    @Operation(summary = "Get budget by ID", description = "Retrieve a specific budget by its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Budget found"),
            @ApiResponse(responseCode = "404", description = "Budget not found")
    })
    @GetMapping("/{id}")
    public BudgetResponseDTO getBudgetById(@PathVariable Long id) {
        return service.getBudgetById(id);
    }

    @Operation(summary = "Get budget summary", description = "Get calculated totals for allocated and spent amounts.")
    @GetMapping("/summary")
    public BudgetSummaryDTO getBudgetSummary() {
        return service.getBudgetSummary();
    }

    @Operation(summary = "Create a new budget", description = "Add a new budget entry.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetResponseDTO createBudget(@Valid @RequestBody BudgetRequestDTO request) {
        return service.createBudget(request);
    }

    @Operation(summary = "Update an existing budget", description = "Update details of an existing budget by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Budget updated"),
            @ApiResponse(responseCode = "404", description = "Budget not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public BudgetResponseDTO updateBudget(@PathVariable Long id, @Valid @RequestBody BudgetRequestDTO request) {
        return service.updateBudget(id, request);
    }

    @Operation(summary = "Delete a budget", description = "Remove a budget entry.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBudgetById(@PathVariable Long id) {
        service.deleteBudget(id);
    }

    @DeleteMapping("/delete-all")
    @Operation(
            summary = "Delete All Budgets",
            description = "Remove all budgets at once. Think before using this."
    )
    @ApiResponse(responseCode = "204", description = "All budgets deleted successfully")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllBudget() {
        service.deleteAllBudgets();
    }

    //new
    @PutMapping("/spent/{id}")
    public void addSpentAmount(@PathVariable Long id, BigDecimal amount) {
         service.addSpentAmount(id,amount);
    }

}