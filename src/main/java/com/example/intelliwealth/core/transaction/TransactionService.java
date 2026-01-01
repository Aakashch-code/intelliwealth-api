package com.example.intelliwealth.core.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

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

    /**
     * Calculates average monthly expense over the specified last 'n' months.
     * Used by Contingency Module to calculate Burn Rate.
     */
    public BigDecimal getAverageMonthlyExpense(int monthsToLookBack) {
        // 1. Calculate the cut-off date (e.g., Today minus 3 months)
        LocalDate cutoffDate = LocalDate.now().minusMonths(monthsToLookBack);

        // 2. Fetch only EXPENSES after that date
        List<Transaction> recentExpenses = repo.findByTypeAndTransactionDateAfter("EXPENSE", cutoffDate);

        if (recentExpenses.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // 3. Sum them up
        BigDecimal totalExpense = recentExpenses.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. Divide by number of months to get the Average
        // We use Math.max(1, ...) to prevent division by zero or skewed data if user is new
        return totalExpense.divide(new BigDecimal(monthsToLookBack), 2, RoundingMode.HALF_UP);
    }
}
