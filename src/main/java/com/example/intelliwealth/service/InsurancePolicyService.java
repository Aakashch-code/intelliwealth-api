package com.example.intelliwealth.service;

import com.example.intelliwealth.model.InsurancePolicy;
import com.example.intelliwealth.repository.InsurancePolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsurancePolicyService {

    @Autowired
    private InsurancePolicyRepository repo;

    public List<InsurancePolicy> getAllPolicy() {
        return repo.findAll();
    }
}
