package com.example.intelliwealth.controller;

import com.example.intelliwealth.model.Debt;
import com.example.intelliwealth.service.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class DebtController {

    @Autowired
    private DebtService service;

    @GetMapping("/debt")
    public List<Debt> getAllDebt() {
        return service.getAllDebt();
    }

}
