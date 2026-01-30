package com.example.intelliwealth.treasury.budget.api;

import com.example.intelliwealth.treasury.budget.application.dto.BudgetRequestDTO;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetResponseDTO;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetSummaryDTO;
import com.example.intelliwealth.treasury.budget.application.service.BudgetExportService;
import com.example.intelliwealth.treasury.budget.application.service.BudgetService;
import com.example.intelliwealth.treasury.transaction.application.dto.TransactionResponse;
import com.example.intelliwealth.treasury.transaction.application.service.TransactionExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/budget")
@Tag(name = "Budget Controller", description = "Management APIs for Budgeting System")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService service;

    @Operation(summary = "Get all budgets", description = "Retrieve a list of all budget entries for the current user.")
    @GetMapping
    public List<BudgetResponseDTO> getAllBudgets() {
        return service.getAllBudgets();
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
    @ResponseStatus(HttpStatus.CREATED) // Explicit 201 Created
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
    @ResponseStatus(HttpStatus.NO_CONTENT) // Explicit 204 No Content
    public void deleteBudgetById(@PathVariable Long id) {
        service.deleteBudgetById(id);
    }
}