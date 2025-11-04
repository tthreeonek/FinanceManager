package com.finance.service;

import com.finance.model.User;
import com.finance.model.Budget;

import java.util.Optional;

public class BudgetService {

    public void setBudget(User user, String category, double limit) {
        ValidationService.validateCategory(category);
        if (limit < 0) {
            throw new IllegalArgumentException("Лимит бюджета не может быть отрицательным");
        }
        user.getWallet().setBudget(category, limit);
    }

    public Optional<Budget> getBudget(User user, String category) {
        return user.getWallet().getBudget(category);
    }

    public double getRemainingBudget(User user, String category) {
        Optional<Budget> budget = user.getWallet().getBudget(category);
        if (budget.isPresent()) {
            double spent = new TransactionService().getExpensesByCategory(user, category);
            return budget.get().getLimit() - spent;
        }
        return 0.0;
    }
}