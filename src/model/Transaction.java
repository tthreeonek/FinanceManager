package com.finance.model;

import java.time.LocalDateTime;
import java.io.Serializable;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String category;
    private double amount;
    private TransactionType type;
    private LocalDateTime date;
    private String description;

    public Transaction(String category, double amount, TransactionType type, String description) {
        this.id = java.util.UUID.randomUUID().toString();
        this.category = category;
        this.amount = amount;
        this.type = type;
        this.date = LocalDateTime.now();
        this.description = description;
    }

    // Getters
    public String getId() { return id; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public LocalDateTime getDate() { return date; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return String.format("Transaction{id='%s', category='%s', amount=%.2f, type=%s, date=%s}",
                id, category, amount, type, date);
    }
}