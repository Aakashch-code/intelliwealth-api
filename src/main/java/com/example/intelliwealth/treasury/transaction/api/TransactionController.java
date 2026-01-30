package com.example.intelliwealth.treasury.transaction.api;

import com.example.intelliwealth.treasury.transaction.application.dto.SavingResponse;
import com.example.intelliwealth.treasury.transaction.application.dto.TransactionRequest;
import com.example.intelliwealth.treasury.transaction.application.dto.TransactionResponse;
import com.example.intelliwealth.treasury.transaction.application.service.TransactionExportService;
import com.example.intelliwealth.treasury.transaction.application.service.TransactionService;
import com.example.intelliwealth.treasury.transaction.domain.model.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Financial transaction management")
public class TransactionController {

    @Autowired
    private final TransactionExportService exportService;

    @Autowired
    private final TransactionService service;

    @GetMapping
    @Operation(summary = "Search transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(service.getTransactions(keyword));
    }

    @PostMapping
    @Operation(summary = "Create transaction")
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody @Valid TransactionRequest request) {
        return new ResponseEntity<>(service.createTransaction(request), HttpStatus.CREATED);
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
        return ResponseEntity.ok(service.updateTransaction(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete transaction")
    public void deleteTransaction(@PathVariable Long id) {
        service.deleteTransaction(id);
    }

    @GetMapping("/summary/net")
    @Operation(summary = "Get net financial position")
    public ResponseEntity<SavingResponse> getNetSummary() {
        return ResponseEntity.ok(service.calculateNetPosition());
    }
}