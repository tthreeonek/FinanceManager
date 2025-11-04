package com.finance.service;

import com.finance.model.User;
import com.finance.model.Transaction;
import com.finance.model.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FilterService {

    public List<Transaction> filterTransactions(User user, List<String> categories,
                                                LocalDate startDate, LocalDate endDate,
                                                TransactionType type) {
        return user.getWallet().getTransactions().stream()
                .filter(t -> type == null || t.getType() == type)
                .filter(t -> categories == null || categories.isEmpty() ||
                        categories.stream().anyMatch(cat ->
                                cat.equalsIgnoreCase(t.getCategory())))
                .filter(t -> startDate == null ||
                        t.getDate().toLocalDate().isAfter(startDate.minusDays(1)))
                .filter(t -> endDate == null ||
                        t.getDate().toLocalDate().isBefore(endDate.plusDays(1)))
                .collect(Collectors.toList());
    }

    public double calculateTotalByFilter(User user, List<String> categories,
                                         LocalDate startDate, LocalDate endDate,
                                         TransactionType type) {
        return filterTransactions(user, categories, startDate, endDate, type)
                .stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public void printFilteredStatistics(User user, List<String> categories,
                                        LocalDate startDate, LocalDate endDate) {
        List<Transaction> filteredTransactions = filterTransactions(user, categories, startDate, endDate, null);

        if (filteredTransactions.isEmpty()) {
            System.out.println("⚠️  Нет данных по указанным критериям фильтрации");
            return;
        }

        double totalIncome = filteredTransactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpense = filteredTransactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();

        System.out.println("\n📊 СТАТИСТИКА ПО ФИЛЬТРУ:");
        System.out.println("═".repeat(50));
        System.out.printf("Период: %s - %s%n",
                startDate != null ? startDate : "начало",
                endDate != null ? endDate : "конец");
        System.out.printf("Категории: %s%n",
                categories != null && !categories.isEmpty() ? String.join(", ", categories) : "все");
        System.out.printf("Всего операций: %d%n", filteredTransactions.size());
        System.out.printf("Общий доход: %,12.2f%n", totalIncome);
        System.out.printf("Общие расходы: %,12.2f%n", totalExpense);
        System.out.printf("Баланс: %,12.2f%n", totalIncome - totalExpense);
    }
}