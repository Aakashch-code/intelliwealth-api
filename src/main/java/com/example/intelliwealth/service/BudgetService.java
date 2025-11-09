package com.example.intelliwealth.service;

import com.example.intelliwealth.model.Budget;
import com.example.intelliwealth.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BudgetService {

    @Autowired
    private BudgetRepository repo;

    //Get
    public List<Budget> getAllBudget() {
        return repo.findAll();
    }
    public Budget getBudgetById(int budgetNo) {
        return repo.findById(budgetNo).orElse(new Budget());
    }

    //Put
    public Budget updateBudget(Budget budgetNo) {
        return repo.save(budgetNo);
    }

    //Post
    public Budget createBudget(Budget budget) {
        return repo.save(budget);
    }

    //Delete
    public void deleteBudgetById(int budgetNo) {
        repo.deleteById(budgetNo);
    }
    public void deleteAllBudget() {
        repo.deleteAll();
    }
}
