package com.example.intelliwealth.controller;

import com.example.intelliwealth.model.Transaction;
import com.example.intelliwealth.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService service;

    // GET

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return service.getAllTransactions();
    }

    @GetMapping("/{transactionNum}")
    public Transaction getTransactionById(@PathVariable int transactionNum) {
        return service.getTransactionById(transactionNum);
    }

    @GetMapping("/type/{type}")
    public List<Transaction> getTransactionByType(@PathVariable String type) {
        return service.getTransactionsByType(type);
    }

    @GetMapping("/total-income")
    public double getIncomeAmount() {
        return service.getIncomeAmount();
    }

    @GetMapping("/total-expense")
    public double getExpenseAmount() {
        return service.getExpenseAmount();
    }

    @GetMapping("/net-amount")
    public double calculateTotalAmount() {
        return service.calculateNetAmount();
    }

    @GetMapping("/search")
    public List<Transaction> searchTransactions(@RequestParam String keyword) {
        return service.searchTransactions(keyword);
    }

    // POST

    @PostMapping
    public Transaction postTransactions(@RequestBody Transaction transaction) {
        return service.createTransaction(transaction);
    }

    // PUT

    @PutMapping("/{id}")
    public Transaction updateTransaction(@PathVariable int id, @RequestBody Transaction transaction) {
        return service.updateTransaction(id, transaction);
    }


    // DELETE

    @DeleteMapping("/{transactionNum}")
    public void deleteTransactionById(@PathVariable int transactionNum) {
        service.deleteTransaction(transactionNum);
    }

    @DeleteMapping("/delete-all-transaction")
    public void deleteAllTransactions() {
        service.deleteAllTransactions();
    }
}
