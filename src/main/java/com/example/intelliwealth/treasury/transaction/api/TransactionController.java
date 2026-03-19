package com.example.intelliwealth.treasury.transaction.api;

import com.example.intelliwealth.treasury.budget.application.dto.AddExpenseRequest;
import com.example.intelliwealth.treasury.transaction.application.dto.SavingResponse;
import com.example.intelliwealth.treasury.transaction.application.dto.TransactionRequest;
import com.example.intelliwealth.treasury.transaction.application.dto.TransactionResponse;
import com.example.intelliwealth.treasury.transaction.infrastructure.export.TransactionExportService;
import com.example.intelliwealth.treasury.transaction.application.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Financial transaction management")
public class TransactionController {

    private final TransactionService service;

    @GetMapping
    @Operation(summary = "Search transactions")
    public ResponseEntity<Page<TransactionResponse>> getTransactions(@RequestParam(required = false) String keyword, Pageable pageable) {
        return ResponseEntity.ok(service.getTransactions(keyword,pageable));
    }

    @PostMapping
    @Operation(summary = "Create transaction")
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody @Valid TransactionRequest request) {
        return new ResponseEntity<>(service.createTransaction(request,null), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction details")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTransactionById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update transaction")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable Long id,
            @RequestBody @Valid TransactionRequest request) {
        return ResponseEntity.ok(service.updateTransaction(id, request,null));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete transaction")
    public void deleteTransaction(@PathVariable Long id, AddExpenseRequest request) {
        service.deleteTransaction(id,request);
    }

    @GetMapping("/summary/net")
    @Operation(summary = "Get net financial position")
    public ResponseEntity<SavingResponse> getNetSummary() {
        return ResponseEntity.ok(service.calculateNetPosition());
    }
}