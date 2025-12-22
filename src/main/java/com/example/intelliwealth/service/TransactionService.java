package com.example.intelliwealth.service;

import com.example.intelliwealth.dto.transactions.TransactionDTO;
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

    @Autowired
    private TransactionsRepository repo;

    @Autowired
    private TransactionMapper mapper;

    // ===================== GET =====================

    public List<TransactionDTO> getAllTransactions() {
        return repo.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public TransactionDTO getTransactionById(int transactionNum) {
        Transaction entity = repo.findById(transactionNum)
                .orElseThrow(() ->
                        new IllegalArgumentException("Transaction not found with id: " + transactionNum));

        return mapper.toDTO(entity);
    }

    public List<TransactionDTO> getTransactionsByType(String type) {
        return repo.findByType(type)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
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

    // ===================== SEARCH =====================

    public List<TransactionDTO> searchTransactions(String keyword) {
        return repo.findByDescriptionContainingIgnoreCase(keyword)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // ===================== POST =====================

    public TransactionDTO createTransaction(TransactionDTO dto) {
        Transaction entity = mapper.toEntity(dto);
        Transaction saved = repo.save(entity);
        return mapper.toDTO(saved);
    }

    // ===================== PUT =====================

    public TransactionDTO updateTransaction(int id, TransactionDTO dto) {
        Transaction existing = repo.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Transaction not found with id: " + id));

        existing.setType(dto.getType());
        existing.setDescription(dto.getDescription());
        existing.setAmount(dto.getAmount());
        existing.setCategory(dto.getCategory());
        existing.setSource(dto.getSource());
        existing.setTransactionId(dto.getTransactionId());
        existing.setCreatedAt(dto.getCreatedAt());

        Transaction updated = repo.save(existing);
        return mapper.toDTO(updated);
    }

    // ===================== DELETE =====================

    public void deleteTransaction(int id) {
        repo.deleteById(id);
    }

    public void deleteAllTransactions() {
        repo.deleteAll();
    }
}
