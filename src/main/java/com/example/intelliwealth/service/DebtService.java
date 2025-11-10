package com.example.intelliwealth.service;

import com.example.intelliwealth.model.Debt;
import com.example.intelliwealth.repository.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtService {

    @Autowired
    private DebtRepository repo;


    public List<Debt> getAllDebt() {
        return repo.findAll();
    }
}
