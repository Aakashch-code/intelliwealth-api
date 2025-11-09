package com.example.intelliwealth.controller;

import com.example.intelliwealth.model.Transaction;
import com.example.intelliwealth.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {
        return service.getAllTransactions();
    }

    @GetMapping("/{transactionNum}")
    public Transaction getTransactionById(@PathVariable int transactionNum) {
        return service.getAllTransactionsbyId(transactionNum);
    }

    @PostMapping("/transactions")
    public Transaction postTransactions(@RequestBody Transaction transaction) {
        return service.postAllTransactions(transaction);
    }

    @PutMapping("/transactions")
    public Transaction updateTransaction(@RequestBody Transaction transaction) {
       return service.updateTransactions(transaction);
    }

    @DeleteMapping("/{transactionNum}")
    public void deleteTransactionById(@PathVariable int transactionNum) {
         service.deleteTransactions(transactionNum);
    }
}
