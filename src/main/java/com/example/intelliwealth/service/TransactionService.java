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

    // GET

    public List<Transaction> getAllTransactions() {
        return repo.findAll();
    }

    public Transaction getTransactionById(int transactionNum) {
        return repo.findById(transactionNum)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found with id: " + transactionNum));
    }

    public List<Transaction> getTransactionsByType(String type) {
        return repo.findByType(type);
    }

    public double getIncomeAmount() {
        return repo.findByType("INCOME").stream()
                .mapToDouble(t -> t.getAmount().doubleValue())
                .sum();
    }

    public double getExpenseAmount() {
        return repo.findByType("EXPENSE").stream()
                .mapToDouble(t -> t.getAmount().doubleValue())
                .sum();
    }

    // POST

    public Transaction createTransaction(Transaction transaction) {
        return repo.save(transaction);
    }

    // PUT

    public Transaction updateTransaction(int id, Transaction transaction) {
        Transaction existing = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found with id: " + id));

        existing.setType(transaction.getType());
        existing.setAmount(transaction.getAmount());
        existing.setDescription(transaction.getDescription());


        return repo.save(existing);
    }

    // DELETE

    public void deleteTransaction(int id) {
        repo.deleteById(id);
    }

    public void deleteAllTransactions() {
        repo.deleteAll();
    }

    // CALCULATIONS

    public double calculateNetAmount() {
        return getIncomeAmount() - getExpenseAmount();
    }

    public List<Transaction> searchTransactions(String keyword) {
        return repo.findByDescriptionContainingIgnoreCase(keyword);
    }
}
