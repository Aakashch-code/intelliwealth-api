package com.example.intelliwealth.treasury.transaction.infrastructure.persistence;

import com.example.intelliwealth.treasury.transaction.domain.model.Transaction;
import com.example.intelliwealth.treasury.transaction.domain.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Changed ID type to Long
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByUserIdOrderByTransactionDateDesc(UUID userId);

    Optional<Transaction> findByIdAndUserId(Long id, UUID userId);

    List<Transaction> findByUserIdAndDescriptionContainingIgnoreCase(UUID userId, String keyword);

//    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.userId = :userId AND t.type = :type")
//    BigDecimal sumAmountByUserIdAndType(@Param("userId") UUID userId, @Param("type") TransactionType type);
//
    @Query("SELECT COALESCE(SUM(t.amount),0) FROM Transaction t where t.userId = :userId AND t.type = :type")
    BigDecimal sumAmountByUserIdAndType(@Param("userId") UUID userId,@Param("type") TransactionType type);

    @Query("SELECT COALESCE(AVG(t.amount), 0) FROM Transaction t WHERE t.userId = :userId AND t.type = 'EXPENSE' AND t.transactionDate >= :startDate")
    BigDecimal getAverageExpenseSince(@Param("userId") UUID userId, @Param("startDate") LocalDate startDate);
}