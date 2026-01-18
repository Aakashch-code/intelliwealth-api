package com.example.intelliwealth.wealth.debt.infrastructure.persistence;

import com.example.intelliwealth.wealth.debt.domain.model.Debt;
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

    long deleteByIdAndUserId(String id, UUID userId);
}
