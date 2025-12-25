package com.example.intelliwealth.controller;

import com.example.intelliwealth.dto.transactions.TransactionRequestDTO;
import com.example.intelliwealth.dto.transactions.TransactionResponseDTO;
import com.example.intelliwealth.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:5173")
public class TransactionController {

    private final TransactionService service;

    @Autowired
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    // ===================== GET =====================

    @GetMapping
    public List<TransactionResponseDTO> getTransactions(
            @RequestParam(required = false) String keyword) {

        if (keyword != null && !keyword.isBlank()) {
            return service.searchTransactions(keyword);
        }
        return service.getAllTransactions();
    }

    @GetMapping("/{id}")
    public TransactionResponseDTO getTransactionById(@PathVariable int id) {
        return service.getTransactionById(id);
    }

    // ===================== SUMMARY =====================

    @GetMapping("/summary")
    public BigDecimal getNetAmount() {
        return service.calculateNetAmount();
    }

    // ===================== POST =====================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponseDTO createTransaction(
            @RequestBody TransactionRequestDTO request) {
        return service.createTransaction(request);
    }

    // ===================== PUT =====================

    @PutMapping("/{id}")
    public TransactionResponseDTO updateTransaction(
            @PathVariable int id,
            @RequestBody TransactionRequestDTO request) {
        return service.updateTransaction(id, request);
    }

    // ===================== DELETE =====================

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearAllTransactions() {
        service.deleteAllTransactions();
    }
}
