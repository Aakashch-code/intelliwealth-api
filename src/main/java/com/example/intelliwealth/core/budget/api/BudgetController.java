package com.example.intelliwealth.core.budget.api;

import com.example.intelliwealth.core.budget.application.dto.BudgetRequestDTO;
import com.example.intelliwealth.core.budget.application.dto.BudgetResponseDTO;
import com.example.intelliwealth.core.budget.application.dto.BudgetSummaryDTO;
import com.example.intelliwealth.core.budget.application.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/budget")
@Tag(name = "Budget Controller", description = "Management APIs for Budgeting System")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService service;

    @Operation(summary = "Get all budgets", description = "Retrieve a list of all budget entries.")
    @ApiResponse(responseCode = "200", description = "Found the budgets")
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
    public BudgetResponseDTO getBudgetById(@PathVariable int id) {
        return service.getBudgetById(id);
    }

    @Operation(summary = "Get budget summary", description = "Get the calculated totals for allocated and spent amounts.")
    @ApiResponse(responseCode = "200", description = "Successfully calculated summary")
    @GetMapping("/summary")
    public BudgetSummaryDTO getBudgetSummary() {
        return service.getBudgetSummary();
    }

    @Operation(summary = "Create a new budget", description = "Add a new budget entry to the database.")
    @ApiResponse(responseCode = "200", description = "Budget created successfully")
    @PostMapping
    public BudgetResponseDTO createBudget(@RequestBody BudgetRequestDTO request) {
        return service.createBudget(request);
    }

    @Operation(summary = "Update an existing budget", description = "Update the details of an existing budget by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Budget updated successfully"),
            @ApiResponse(responseCode = "404", description = "Budget to update not found")
    })
    @PutMapping("/{id}")
    public BudgetResponseDTO updateBudget(@PathVariable int id, @RequestBody BudgetRequestDTO request) {
        return service.updateBudget(id, request);
    }

    @Operation(summary = "Delete a budget", description = "Remove a budget entry from the database by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Budget deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Budget to delete not found")
    })
    @DeleteMapping("/{id}")
    public void deleteBudgetById(@PathVariable int id) {
        service.deleteBudgetById(id);
    }
}