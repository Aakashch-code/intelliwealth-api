package com.example.intelliwealth.treasury.transaction.application.service;

import com.example.intelliwealth.authentication.application.SecuredService;
import com.example.intelliwealth.treasury.transaction.application.dto.SavingResponse;
import com.example.intelliwealth.treasury.transaction.application.dto.TransactionRequest;
import com.example.intelliwealth.treasury.transaction.application.dto.TransactionResponse;
import com.example.intelliwealth.treasury.transaction.application.mapper.TransactionMapper;
import com.example.intelliwealth.treasury.transaction.domain.exception.TransactionNotFoundException;
import com.example.intelliwealth.treasury.transaction.domain.model.Transaction;
import com.example.intelliwealth.treasury.transaction.domain.model.TransactionType;
import com.example.intelliwealth.treasury.transaction.domain.validation.TransactionDomainValidator;
import com.example.intelliwealth.treasury.transaction.infrastructure.persistence.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("isAuthenticated()")
public class TransactionService extends SecuredService {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;
    private final TransactionDomainValidator validator;

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactions(String keyword) {
        UUID userId = currentUserId();
        List<Transaction> transactions = (keyword != null && !keyword.isBlank())
                ? repository.findByUserIdAndDescriptionContainingIgnoreCase(userId, keyword)
                : repository.findAllByUserIdOrderByTransactionDateDesc(userId);

        return transactions.stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Long id) { // Changed Integer to Long
        return repository.findByIdAndUserId(id, currentUserId())
                .map(mapper::toResponse)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found: " + id));
    }

    public TransactionResponse createTransaction(TransactionRequest request) {
        // Map first, then validate the DOMAIN object
        Transaction transaction = mapper.toEntity(request);
        transaction.setUserId(currentUserId());

        // Domain validation checks business rules (e.g., "No future dates")
        validator.validate(transaction);

        return mapper.toResponse(repository.save(transaction));
    }

    public TransactionResponse updateTransaction(Long id, TransactionRequest request) {
        Transaction existing = repository.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found: " + id));

        // Update fields
        mapper.updateEntityFromRequest(request, existing);

        // Re-validate the merged entity
        validator.validate(existing);

        return mapper.toResponse(repository.save(existing));
    }

    public void deleteTransaction(Long id) {
        Transaction existing = repository.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found: " + id));
        repository.delete(existing);
    }

    @Transactional(readOnly = true)
    public SavingResponse calculateNetPosition() {
        UUID userId = currentUserId();
        BigDecimal income = repository.sumAmountByUserIdAndType(userId, TransactionType.INCOME);
        BigDecimal expense = repository.sumAmountByUserIdAndType(userId, TransactionType.EXPENSE);
        BigDecimal saving  = income.subtract(expense);
        return new SavingResponse(income, expense, saving);
    }

    @Transactional(readOnly = true)
    public BigDecimal getMonthlyAverageExpense(int months) {
        if (months <= 0) throw new IllegalArgumentException("Months must be > 0");

        BigDecimal average = repository.getAverageExpenseSince(currentUserId(), LocalDate.now().minusMonths(months));

        // Add rounding to prevent API displaying 1200.33333333
        return average.setScale(2, RoundingMode.HALF_UP);
    }
}