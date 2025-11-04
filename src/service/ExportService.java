package com.finance.service;

import com.finance.model.User;
import com.finance.model.Transaction;
import com.finance.model.TransactionType;
import com.finance.model.Budget;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportService {

    public void exportToCsv(User user, String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            // Заголовок
            writer.write("Дата;Категория;Тип;Сумма;Описание\n");

            // Данные
            for (Transaction transaction : user.getWallet().getTransactions()) {
                writer.write(String.format("%s;%s;%s;%.2f;%s\n",
                        transaction.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        transaction.getCategory(),
                        transaction.getType() == TransactionType.INCOME ? "Доход" : "Расход",
                        transaction.getAmount(),
                        transaction.getDescription() != null ? transaction.getDescription() : ""
                ));
            }
        }
    }

    public void exportToJson(User user, String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("{\n");
            writer.write("  \"username\": \"" + escapeJson(user.getUsername()) + "\",\n");
            writer.write("  \"balance\": " + user.getWallet().getBalance() + ",\n");

            // Транзакции
            writer.write("  \"transactions\": [\n");
            List<Transaction> transactions = user.getWallet().getTransactions();
            for (int i = 0; i < transactions.size(); i++) {
                Transaction t = transactions.get(i);
                writer.write("    {\n");
                writer.write("      \"id\": \"" + escapeJson(t.getId()) + "\",\n");
                writer.write("      \"category\": \"" + escapeJson(t.getCategory()) + "\",\n");
                writer.write("      \"amount\": " + t.getAmount() + ",\n");
                writer.write("      \"type\": \"" + t.getType() + "\",\n");
                writer.write("      \"date\": \"" + t.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\",\n");
                writer.write("      \"description\": \"" + escapeJson(t.getDescription() != null ? t.getDescription() : "") + "\"\n");
                writer.write("    }" + (i < transactions.size() - 1 ? "," : "") + "\n");
            }
            writer.write("  ],\n");

            // Бюджеты
            writer.write("  \"budgets\": [\n");
            List<Budget> budgets = user.getWallet().getBudgets();
            for (int i = 0; i < budgets.size(); i++) {
                Budget b = budgets.get(i);
                writer.write("    {\n");
                writer.write("      \"category\": \"" + escapeJson(b.getCategory()) + "\",\n");
                writer.write("      \"limit\": " + b.getLimit() + "\n");
                writer.write("    }" + (i < budgets.size() - 1 ? "," : "") + "\n");
            }
            writer.write("  ]\n");
            writer.write("}\n");
        }
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public void importFromJson(String filename) {
        System.out.println("⚠️  Импорт из JSON временно недоступен. Используйте CSV для экспорта.");
    }
}