package com.finance.service;

import com.finance.model.Transaction;
import com.finance.model.TransactionType;
import com.finance.model.User;
import com.finance.model.Budget;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionService {

    public void addIncome(User user, String category, double amount, String description) {
        ValidationService.validateAmount(amount);
        ValidationService.validateCategory(category);
        ValidationService.validateDescription(description);

        Transaction transaction = new Transaction(category, amount, TransactionType.INCOME, description);
        user.getWallet().addTransaction(transaction);
    }

    public void addExpense(User user, String category, double amount, String description) {
        ValidationService.validateAmount(amount);
        ValidationService.validateCategory(category);
        ValidationService.validateDescription(description);

        Transaction transaction = new Transaction(category, amount, TransactionType.EXPENSE, description);
        user.getWallet().addTransaction(transaction);
    }

    public double getTotalIncome(User user) {
        return user.getWallet().getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpenses(User user) {
        return user.getWallet().getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getIncomeByCategory(User user, String category) {
        return user.getWallet().getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.INCOME &&
                        t.getCategory().equalsIgnoreCase(category))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getExpensesByCategory(User user, String category) {
        return user.getWallet().getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE &&
                        t.getCategory().equalsIgnoreCase(category))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public List<String> getIncomeCategories(User user) {
        return user.getWallet().getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getExpenseCategories(User user) {
        return user.getWallet().getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Budget> checkBudgetExceedance(User user) {
        return user.getWallet().getBudgets().stream()
                .filter(budget -> {
                    double spent = getExpensesByCategory(user, budget.getCategory());
                    return spent > budget.getLimit();
                })
                .collect(Collectors.toList());
    }
}