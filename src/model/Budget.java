package com.finance.model;

import java.io.Serializable;

public class Budget implements Serializable {
    private static final long serialVersionUID = 1L;

    private String category;
    private double limit;

    public Budget(String category, double limit) {
        this.category = category;
        this.limit = limit;
    }

    // Getters and Setters
    public String getCategory() { return category; }
    public double getLimit() { return limit; }
    public void setLimit(double limit) { this.limit = limit; }

    @Override
    public String toString() {
        return String.format("Budget{category='%s', limit=%.2f}", category, limit);
    }
}