package com.example.intelliwealth.wealth.debt.infrastructure.persistence;

import com.example.intelliwealth.wealth.debt.application.dto.DebtSumProjection;
import com.example.intelliwealth.wealth.debt.domain.model.DebtStatus;
import com.example.intelliwealth.wealth.debt.domain.model.Debt;
import org.bson.types.Decimal128;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DebtRepository extends MongoRepository<Debt, String> {

    List<Debt> findAllByUserId(UUID userId);

    Optional<Debt> findByIdAndUserId(String id, UUID userId);
    List<Debt> findAllByUserIdAndStatus(UUID userId, DebtStatus status);

    @Aggregation(pipeline = {
            "{ $match: { userId: ?0 } }",
            "{ $group: { _id: null, totalValue: { $sum: '$totalAmount' } } }"
    })
    DebtSumProjection sumOfTotalDebtByUserId(UUID userId);

    long deleteByIdAndUserId(String id, UUID userId);
}
