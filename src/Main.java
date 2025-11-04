package com.finance;

import com.finance.cli.FinanceCLI;

public class Main {
    public static void main(String[] args) {
        try {
            FinanceCLI cli = new FinanceCLI();
            cli.start();
        } catch (Exception e) {
            System.err.println("❌ Критическая ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}