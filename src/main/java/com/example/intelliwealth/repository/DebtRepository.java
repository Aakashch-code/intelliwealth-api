package com.example.intelliwealth.repository;

import com.example.intelliwealth.model.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtRepository extends JpaRepository<Debt,Integer> {
}
