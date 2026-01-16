package com.example.intelliwealth.core.transaction.application.service;

import com.example.intelliwealth.authentication.application.SecuredService;
import com.example.intelliwealth.core.transaction.application.dto.TransactionRequest;
import com.example.intelliwealth.core.transaction.application.dto.TransactionResponse;
import com.example.intelliwealth.core.transaction.application.mapper.TransactionMapper;
import com.example.intelliwealth.core.transaction.domain.exception.TransactionNotFoundException;
import com.example.intelliwealth.core.transaction.domain.model.Transaction;
import com.example.intelliwealth.core.transaction.domain.model.TransactionType;
import com.example.intelliwealth.core.transaction.domain.validation.TransactionDomainValidator;
import com.example.intelliwealth.core.transaction.infrastructure.persistence.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public TransactionResponse getTransactionById(Integer id) {
        return repository.findByIdAndUserId(id, currentUserId())
                .map(mapper::toResponse)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found: " + id));
    }

    public TransactionResponse createTransaction(TransactionRequest request) {
        validator.validate(request);

        Transaction transaction = mapper.toEntity(request);
        transaction.setUserId(currentUserId());

        return mapper.toResponse(repository.save(transaction));
    }

    public TransactionResponse updateTransaction(Integer id, TransactionRequest request) {
        validator.validate(request);

        Transaction existing = repository.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found: " + id));

        mapper.updateEntityFromRequest(request, existing);

        return mapper.toResponse(repository.save(existing));
    }

    public void deleteTransaction(Integer id) {
        Transaction existing = repository.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found: " + id));
        repository.delete(existing);
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateNetPosition() {
        UUID userId = currentUserId();
        BigDecimal income = repository.sumAmountByUserIdAndType(userId, TransactionType.INCOME);
        BigDecimal expense = repository.sumAmountByUserIdAndType(userId, TransactionType.EXPENSE);
        return income.subtract(expense);
    }

    @Transactional(readOnly = true)
    public BigDecimal getMonthlyAverageExpense(int months) {
        if (months <= 0) throw new IllegalArgumentException("Months must be > 0");
        return repository.getAverageExpenseSince(currentUserId(), LocalDate.now().minusMonths(months));
    }
}