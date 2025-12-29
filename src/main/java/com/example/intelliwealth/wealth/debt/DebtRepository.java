package com.example.intelliwealth.wealth.debt;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtRepository extends MongoRepository<Debt, String> {
}
