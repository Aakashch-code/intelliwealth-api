package com.example.intelliwealth.wealth.debt.repository;

import com.example.intelliwealth.wealth.debt.domain.Debt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtRepository extends MongoRepository<Debt, String> {
}
