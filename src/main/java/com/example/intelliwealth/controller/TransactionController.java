package com.example.intelliwealth.controller;

import com.example.intelliwealth.dto.transactions.TransactionDTO;
import com.example.intelliwealth.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/transactions")
@Tag(
        name = "Transaction Management",
        description = "Operations related to income and expense tracking"
)
public class TransactionController {

    @Autowired
    private TransactionService service;

    // ===================== GET RESOURCES =====================

    @Operation(
            summary = "Retrieve transactions",
            description = "Get all transactions, or filter them by a keyword search."
    )
    @GetMapping
    public List<TransactionDTO> getTransactions(
            @Parameter(description = "Optional keyword to filter results", example = "Grocery")
            @RequestParam(required = false) String keyword) {

        // If keyword exists, search; otherwise, get all.
        // You might need to adjust your service to handle this logic,
        // or call the specific service method here based on the param.
        if (keyword != null && !keyword.isEmpty()) {
            return service.searchTransactions(keyword);
        }
        return service.getAllTransactions();
    }

    @Operation(
            summary = "Get transaction by ID",
            description = "Retrieve a specific transaction using its unique ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction found"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @GetMapping("/{id}")
    public TransactionDTO getTransactionById(@PathVariable int id) {
        return service.getTransactionById(id);
    }

    // ===================== ANALYTICS / AGGREGATES =====================

    @Operation(
            summary = "Get financial summary",
            description = "Calculates the net amount (Income - Expenses)."
    )
    @GetMapping("/summary") // Naming it "summary" allows you to add Total Income/Expense later easily
    public BigDecimal getFinancialSummary() {
        return service.calculateNetAmount();
    }

    // ===================== POST RESOURCES =====================

    @Operation(
            summary = "Create transaction",
            description = "Creates a new income or expense transaction."
    )
    @PostMapping
    public TransactionDTO createTransaction(@RequestBody TransactionDTO transaction) {
        return service.createTransaction(transaction);
    }

    // ===================== DELETE RESOURCES =====================

    @Operation(
            summary = "Clear transaction history",
            description = "Removes all transactions from the database."
    )
    @DeleteMapping
    public void clearAllTransactions() {
        service.deleteAllTransactions();
    }
}