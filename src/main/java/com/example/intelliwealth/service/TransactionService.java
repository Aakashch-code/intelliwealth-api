package com.example.intelliwealth.service;

import com.example.intelliwealth.model.Transaction;
import com.example.intelliwealth.repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionsRepository repo;

    public List<Transaction> getAllTransactions() {
        return repo.findAll();
    }
    public Transaction postAllTransactions(Transaction transaction) {
        return repo.save(transaction);
    }
    public Transaction updateTransactions(Transaction transaction) {
        return repo.save(transaction);
    }
    public void deleteTransactions(int transaction) {
        repo.deleteById(transaction);
    }
    public Transaction getAllTransactionsbyId(int transactionNum) {
        return repo.findById(transactionNum).orElse(new Transaction());
    }
}
