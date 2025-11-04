package com.finance.service;

import com.finance.model.User;
import com.finance.model.Budget;

import java.util.ArrayList;
import java.util.List;

public class EnhancedAlertService {
    private TransactionService transactionService;
    private BudgetService budgetService;

    public EnhancedAlertService(TransactionService transactionService, BudgetService budgetService) {
        this.transactionService = transactionService;
        this.budgetService = budgetService;
    }

    public List<String> checkAlerts(User user) {
        List<String> alerts = new ArrayList<>();

        // Проверка отрицательного баланса
        if (user.getWallet().getBalance() < 0) {
            alerts.add("❌ ВНИМАНИЕ: Расходы превысили доходы! Баланс отрицательный: " +
                    String.format("%,.2f", user.getWallet().getBalance()));
        }

        // Проверка нулевого баланса
        if (user.getWallet().getBalance() == 0) {
            alerts.add("⚠️  Внимание: Баланс равен нулю");
        }

        // Проверка бюджетов
        for (Budget budget : user.getWallet().getBudgets()) {
            double spent = transactionService.getExpensesByCategory(user, budget.getCategory());
            double limit = budget.getLimit();
            double percentage = (spent / limit) * 100;

            if (spent > limit) {
                alerts.add("❌ Превышен бюджет в категории '" + budget.getCategory() +
                        "': Потрачено " + String.format("%,.2f", spent) +
                        " из " + String.format("%,.2f", limit));
            } else if (percentage >= 80) {
                alerts.add("⚠️  Близко к превышению бюджета '" + budget.getCategory() +
                        "': Потрачено " + String.format("%,.2f", spent) +
                        " (" + String.format("%.0f", percentage) + "%) из " +
                        String.format("%,.2f", limit));
            }
        }

        return alerts;
    }
}