package com.finance.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Wallet implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private double balance;
    private List<Transaction> transactions;
    private List<Budget> budgets;

    public Wallet(String userId) {
        this.userId = userId;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
        this.budgets = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        if (transaction.getType() == TransactionType.INCOME) {
            balance += transaction.getAmount();
        } else {
            balance -= transaction.getAmount();
        }
    }

    public void setBudget(String category, double limit) {
        Optional<Budget> existingBudget = budgets.stream()
                .filter(b -> b.getCategory().equalsIgnoreCase(category))
                .findFirst();

        if (existingBudget.isPresent()) {
            existingBudget.get().setLimit(limit);
        } else {
            budgets.add(new Budget(category, limit));
        }
    }

    public Optional<Budget> getBudget(String category) {
        return budgets.stream()
                .filter(b -> b.getCategory().equalsIgnoreCase(category))
                .findFirst();
    }

    // Getters
    public String getUserId() { return userId; }
    public double getBalance() { return balance; }
    public List<Transaction> getTransactions() { return new ArrayList<>(transactions); }
    public List<Budget> getBudgets() { return new ArrayList<>(budgets); }

    @Override
    public String toString() {
        return String.format("Wallet{userId='%s', balance=%.2f, transactions=%d, budgets=%d}",
                userId, balance, transactions.size(), budgets.size());
    }
}