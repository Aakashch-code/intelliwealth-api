package com.example.intelliwealth.core.transaction;

import com.example.intelliwealth.authentication.security.SecuredService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("isAuthenticated()")
public class TransactionService extends SecuredService {

    private final TransactionsRepository repo;
    private final TransactionMapper mapper;

    // ===================== READ =====================

    public List<TransactionResponseDTO> getAllTransactions() {
        return repo.findAllByUserId(currentUserId())
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public TransactionResponseDTO getTransactionById(int id) {
        return repo.findByIdAndUserId(id, currentUserId())
                .map(mapper::toResponse)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
    }

    public List<TransactionResponseDTO> searchTransactions(String keyword) {
        return repo.findByUserIdAndDescriptionContainingIgnoreCase(
                        currentUserId(), keyword
                )
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // ===================== CALCULATIONS =====================

    public BigDecimal getIncomeAmount() {
        return repo.findByUserIdAndType(currentUserId(), "INCOME")
                .stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getExpenseAmount() {
        return repo.findByUserIdAndType(currentUserId(), "EXPENSE")
                .stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateNetAmount() {
        return getIncomeAmount().subtract(getExpenseAmount());
    }

    // ===================== CREATE =====================

    public TransactionResponseDTO createTransaction(TransactionRequestDTO request) {
        Transaction entity = mapper.toEntity(request);
        entity.setUserId(currentUserId()); // 🔐 owner

        return mapper.toResponse(repo.save(entity));
    }

    // ===================== UPDATE =====================

    public TransactionResponseDTO updateTransaction(int id, TransactionRequestDTO request) {
        Transaction existing = repo.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));

        existing.setType(request.getType());
        existing.setDescription(request.getDescription());
        existing.setAmount(request.getAmount());
        existing.setCategory(request.getCategory());
        existing.setSource(request.getSource());

        return mapper.toResponse(repo.save(existing));
    }

    // ===================== DELETE =====================

    public void deleteAllTransactions() {
        repo.deleteAllByUserId(currentUserId());
    }

    // ===================== ANALYTICS =====================

    /**
     * Calculates average monthly expense over the last N months.
     * Used for burn-rate / contingency calculations.
     */
    public BigDecimal getAverageMonthlyExpense(int monthsToLookBack) {
        LocalDate cutoffDate = LocalDate.now().minusMonths(monthsToLookBack);

        List<Transaction> recentExpenses =
                repo.findByUserIdAndTypeAndTransactionDateAfter(
                        currentUserId(),
                        "EXPENSE",
                        cutoffDate
                );

        if (recentExpenses.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalExpense = recentExpenses.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalExpense.divide(
                BigDecimal.valueOf(Math.max(1, monthsToLookBack)),
                2,
                RoundingMode.HALF_UP
        );
    }
}
