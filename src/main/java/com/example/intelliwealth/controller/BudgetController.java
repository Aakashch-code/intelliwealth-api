package com.example.intelliwealth.controller;

import com.example.intelliwealth.model.Budget;
import com.example.intelliwealth.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class BudgetController {

    @Autowired
    private BudgetService service;

    //Get Method for extracting
    @GetMapping("/budget")
    public List<Budget> getAllBudget() {
        return service.getAllBudget();
    }
    @GetMapping("/budget/{budgetNo}")
    public Budget getBudgetById(@PathVariable int budgetNo) {
        return service.getBudgetById(budgetNo);
    }

    //Put Method for update
    @PutMapping("/budget/{budgetNo}")
    public Budget updateBudgetById(@RequestBody Budget budgetNo) {
        return service.updateBudget(budgetNo);
    }

    //Post Method for creating Budget
    @PostMapping("/budget")
    public Budget createBudget(@RequestBody Budget budget) {
        return service.createBudget(budget);
    }

    //Delete Method
    @DeleteMapping("/budget/{budgetNo}")
    public void deleteBudgetById(@PathVariable int budgetNo) {
         service.deleteBudgetById(budgetNo);
    }
    @DeleteMapping("/budget")
    public void deleteAllBudget() {
        service.deleteAllBudget();
    }
}
