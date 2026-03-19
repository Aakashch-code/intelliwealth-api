package com.example.intelliwealth.treasury.transaction.application.service;

import com.example.intelliwealth.authentication.application.service.SecuredService;
import com.example.intelliwealth.treasury.budget.application.dto.AddExpenseRequest;
import com.example.intelliwealth.treasury.budget.application.service.BudgetService;
import com.example.intelliwealth.treasury.transaction.application.dto.SavingResponse;
import com.example.intelliwealth.treasury.transaction.application.dto.TransactionRequest;
import com.example.intelliwealth.treasury.transaction.application.dto.TransactionResponse;
import com.example.intelliwealth.treasury.transaction.domain.exception.TransactionNotFoundException;
import com.example.intelliwealth.treasury.transaction.domain.model.Transaction;
import com.example.intelliwealth.treasury.transaction.domain.model.TransactionType;
import com.example.intelliwealth.treasury.transaction.domain.validation.TransactionDomainValidator;
import com.example.intelliwealth.treasury.transaction.infrastructure.mapper.TransactionMapper;
import com.example.intelliwealth.treasury.transaction.infrastructure.persistence.TransactionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("isAuthenticated()")
public class TransactionService extends SecuredService {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;
    private final TransactionDomainValidator validator;
    private final BudgetService budgetService;

    // -------- READ OPERATIONS (QUERIES) -------

    @Transactional(readOnly = true)
    public Page<TransactionResponse> getTransactions(String keyword, Pageable pageable) {
        UUID userId = currentUserId();

        Page<Transaction> result = hasKeyword(keyword)
                ? repository.findByUserIdAndDescriptionContainingIgnoreCase(userId, keyword, pageable)
                : repository.findAllByUserIdOrderByTransactionDateDesc(userId, pageable);

        return result.map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Long id) {
        return findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> notFound(id));
    }

    // ------- WRITE OPERATIONS (MUTATIONS) -----

    @CacheEvict(value = "net_position", key = "#root.target.cacheKey()")
    public TransactionResponse createTransaction(TransactionRequest request, AddExpenseRequest expenseRequest) {
        Transaction transaction = mapper.toEntity(request);
        transaction.setUserId(currentUserId());

        validate(transaction);

        // Check if the budget is active before saving
        if (transaction.getBudgetId() != null) {
            budgetService.validateBudgetIsActive(transaction.getBudgetId());
        }

        Transaction saved = repository.save(transaction);

        // Update the budget's spent amount if it's an expense
        if (transaction.getType() == TransactionType.EXPENSE && transaction.getBudgetId() != null) {
            budgetService.addSpentAmount(transaction.getBudgetId(), expenseRequest);
        }

        return mapper.toResponse(saved);
    }

    @CacheEvict(value = "net_position", key = "#root.target.cacheKey()")
    public TransactionResponse updateTransaction(Long id, TransactionRequest request,AddExpenseRequest expenseRequest) {
        Transaction existing = findById(id)
                .orElseThrow(() -> notFound(id));

        if (existing.isSystemGenerated()) {
            throw new IllegalStateException("System generated transactions cannot be modified");
        }

        // Track the old amount to calculate the difference for the budget
        BigDecimal oldAmount = existing.getAmount();


        mapper.updateEntityFromRequest(request, existing);
        validate(existing);

        // If the amount changed, sync the difference to the budget
        if (existing.getType() == TransactionType.EXPENSE && existing.getBudgetId() != null) {
            BigDecimal difference = existing.getAmount().subtract(oldAmount);
            if (difference.compareTo(BigDecimal.ZERO) != 0) {
                budgetService.addSpentAmount(existing.getBudgetId(), expenseRequest);
            }
        }

        return mapper.toResponse(repository.save(existing));
    }

    @CacheEvict(value = "net_position", key = "#root.target.cacheKey()")
    public void deleteTransaction(Long id,AddExpenseRequest request) {
        Transaction transaction = findById(id)
                .orElseThrow(() -> notFound(id));

        if (transaction.isSystemGenerated()) {
            throw new IllegalStateException("System generated transactions cannot be deleted manually.");
        }

        // Refund the amount back to the budget before deleting
        if (transaction.getType() == TransactionType.EXPENSE && transaction.getBudgetId() != null) {
            budgetService.addSpentAmount(transaction.getBudgetId(), request);
        }

        repository.delete(transaction);
    }

    // ---------- SYSTEM OPERATIONS -------------

    @CacheEvict(value = "net_position", key = "#root.target.cacheKey()")
    public void createSystemExpense(Long budgetId, BigDecimal amount, String title, String note) {
        Transaction transaction = new Transaction();

        transaction.setUserId(currentUserId());
        transaction.setBudgetId(budgetId);
        transaction.setAmount(amount);

        // Combine title and note cleanly
        String description = title;
        if (note != null && !note.isBlank()) {
            description += " - " + note;
        }
        transaction.setDescription(description);
        transaction.setType(TransactionType.EXPENSE);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setSystemGenerated(true);

        repository.save(transaction);
    }

    @CacheEvict(value = "net_position", key = "#root.target.cacheKey()")
    public void deleteTransactionsByBudgetId(Long budgetId) {
        repository.deleteByBudgetId(budgetId);
    }

    // -------------- ANALYTICS -----------------

    @Transactional(readOnly = true)
    @Cacheable(value = "net_position", key = "#root.target.cacheKey()")
    public SavingResponse calculateNetPosition() {
        UUID userId = currentUserId();

        BigDecimal income = getSum(userId, TransactionType.INCOME);
        BigDecimal expense = getSum(userId, TransactionType.EXPENSE);

        return new SavingResponse(
                income,
                expense,
                income.subtract(expense)
        );
    }

    @Transactional(readOnly = true)
    public BigDecimal getMonthlyAverageExpense(int months) {
        validateMonths(months);

        BigDecimal avg = repository.getAverageExpenseSince(
                currentUserId(),
                LocalDate.now().minusMonths(months)
        );

        return avg.setScale(2, RoundingMode.HALF_UP);
    }

    // ----------- INTERNAL HELPERS -------------

    private Optional<Transaction> findById(Long id) {
        return repository.findByIdAndUserId(id, currentUserId());
    }

    private BigDecimal getSum(UUID userId, TransactionType type) {
        return Optional.ofNullable(
                repository.sumAmountByUserIdAndType(userId, type)
        ).orElse(BigDecimal.ZERO);
    }

    private void validate(Transaction transaction) {
        validator.validate(transaction);
    }

    private void validateMonths(int months) {
        if (months <= 0) {
            throw new IllegalArgumentException("Months must be greater than 0");
        }
    }

    private boolean hasKeyword(String keyword) {
        return keyword != null && !keyword.isBlank();
    }

    private TransactionNotFoundException notFound(Long id) {
        return new TransactionNotFoundException("Transaction not found: " + id);
    }
}