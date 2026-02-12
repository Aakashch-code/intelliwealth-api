package com.example.intelliwealth.treasury.transaction.application.service;

import com.example.intelliwealth.authentication.application.service.SecuredService;
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
@PreAuthorize("isAuthenticated()")
@Transactional
public class TransactionService extends SecuredService {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;
    private final TransactionDomainValidator validator;

    /* ===================== Read ===================== */

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

    /* ===================== Create ===================== */

    public TransactionResponse createTransaction(TransactionRequest request) {
        Transaction transaction = mapper.toEntity(request);
        transaction.setUserId(currentUserId());

        validate(transaction);

        return mapper.toResponse(repository.save(transaction));
    }

    /* ===================== Update ===================== */

    public TransactionResponse updateTransaction(Long id, TransactionRequest request) {
        Transaction existing = findById(id)
                .orElseThrow(() -> notFound(id));

        mapper.updateEntityFromRequest(request, existing);

        validate(existing);

        return mapper.toResponse(repository.save(existing));
    }

    /* ===================== Delete ===================== */

    public void deleteTransaction(Long id) {
        Transaction transaction = findById(id)
                .orElseThrow(() -> notFound(id));

        repository.delete(transaction);
    }

    /* ===================== Analytics ===================== */

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

    /* ===================== Helpers ===================== */

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
