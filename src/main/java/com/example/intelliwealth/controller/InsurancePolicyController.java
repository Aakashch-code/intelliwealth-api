package com.example.intelliwealth.controller;

import com.example.intelliwealth.model.InsurancePolicy;
import com.example.intelliwealth.service.InsurancePolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class InsurancePolicyController {

    @Autowired
    private InsurancePolicyService service;

    @GetMapping("/insurance-policy")
    private List<InsurancePolicy> getAllPolicy() {
        return service.getAllPolicy();
    }

}
