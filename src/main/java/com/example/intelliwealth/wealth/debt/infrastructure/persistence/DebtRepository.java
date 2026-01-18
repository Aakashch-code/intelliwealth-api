package com.example.intelliwealth.wealth.debt.domain.repository;

import com.example.intelliwealth.wealth.debt.domain.model.Debt;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DebtRepository {
    Debt save(Debt debt);
    List<Debt> findAllByUserId(UUID userId);
    Optional<Debt> findById(String id);
    void deleteByIdAndUserId(String id,UUID userId);
}