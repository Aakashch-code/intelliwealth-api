package com.example.intelliwealth.core.transaction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Transactions", description = "API endpoints for managing financial transactions")
public class TransactionController {

    private final TransactionService service;

    @Autowired
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @Operation(summary = "Get all transactions", description = "Retrieve a list of all transactions or search by keyword.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    public List<TransactionResponseDTO> getTransactions(
            @Parameter(description = "Keyword to search transaction descriptions")
            @RequestParam(required = false) String keyword) {

        if (keyword != null && !keyword.isBlank()) {
            return service.searchTransactions(keyword);
        }
        return service.getAllTransactions();
    }

    @Operation(summary = "Get transaction by ID", description = "Retrieve details of a specific transaction.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @GetMapping("/{id}")
    public TransactionResponseDTO getTransactionById(
            @Parameter(description = "ID of the transaction to retrieve")
            @PathVariable int id) {
        return service.getTransactionById(id);
    }

    @Operation(summary = "Get net amount", description = "Calculate the total net amount of all transactions.")
    @ApiResponse(responseCode = "200", description = "Successfully calculated summary")
    @GetMapping("/summary")
    public BigDecimal getNetAmount() {
        return service.calculateNetAmount();
    }

    @Operation(summary = "Create transaction", description = "Create a new financial transaction.")
    @ApiResponse(responseCode = "201", description = "Transaction created successfully")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponseDTO createTransaction(
            @RequestBody TransactionRequestDTO request) {
        return service.createTransaction(request);
    }

    @Operation(summary = "Update transaction", description = "Update an existing transaction by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @PutMapping("/{id}")
    public TransactionResponseDTO updateTransaction(
            @Parameter(description = "ID of the transaction to update")
            @PathVariable int id,
            @RequestBody TransactionRequestDTO request) {
        return service.updateTransaction(id, request);
    }

    @Operation(summary = "Delete all transactions", description = "Remove all transaction records from the database.")
    @ApiResponse(responseCode = "204", description = "All transactions deleted successfully")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearAllTransactions() {
        service.deleteAllTransactions();
    }
}