package com.example.intelliwealth.service;

import com.example.intelliwealth.dto.transactions.TransactionRequestDTO;
import com.example.intelliwealth.dto.transactions.TransactionResponseDTO;
import com.example.intelliwealth.mapper.transactions.TransactionMapper;
import com.example.intelliwealth.model.Transaction;
import com.example.intelliwealth.repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class TransactionService {

    private final TransactionsRepository repo;
    private final TransactionMapper mapper;

    @Autowired
    public TransactionService(TransactionsRepository repo, TransactionMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    // ===================== GET =====================

    public List<TransactionResponseDTO> getAllTransactions() {
        return repo.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public TransactionResponseDTO getTransactionById(int id) {
        Transaction transaction = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + id));
        return mapper.toResponse(transaction);
    }

    public List<TransactionResponseDTO> searchTransactions(String keyword) {
        return repo.findByDescriptionContainingIgnoreCase(keyword)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // ===================== CALCULATIONS =====================

    public BigDecimal getIncomeAmount() {
        return repo.findByType("INCOME")
                .stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getExpenseAmount() {
        return repo.findByType("EXPENSE")
                .stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateNetAmount() {
        return getIncomeAmount().subtract(getExpenseAmount());
    }

    // ===================== POST =====================

    public TransactionResponseDTO createTransaction(TransactionRequestDTO request) {
        Transaction entity = mapper.toEntity(request);
        Transaction saved = repo.save(entity);
        return mapper.toResponse(saved);
    }

    // ===================== PUT =====================

    public TransactionResponseDTO updateTransaction(int id, TransactionRequestDTO request) {
        Transaction existing = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + id));

        existing.setType(request.getType());
        existing.setDescription(request.getDescription());
        existing.setAmount(request.getAmount());
        existing.setCategory(request.getCategory());
        existing.setSource(request.getSource());

        Transaction updated = repo.save(existing);
        return mapper.toResponse(updated);
    }

    // ===================== DELETE =====================

    public void deleteAllTransactions() {
        repo.deleteAll();
    }
}
