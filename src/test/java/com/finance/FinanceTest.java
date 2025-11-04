package com.finance;

import com.finance.model.User;
import com.finance.model.Transaction;
import com.finance.model.TransactionType;

/**
 * Тесты финансовой логики
 */
public class FinanceTest {

    public static void main(String[] args) {
        System.out.println("=== ТЕСТЫ ФИНАНСОВОЙ ЛОГИКИ ===");

        testUserCreation();
        testTransactionCreation();
        testWalletOperations();
    }

    private static void testUserCreation() {
        try {
            User user = new User("testuser", "password");
            System.out.println("✅ Создание пользователя: УСПЕХ");
        } catch (Exception e) {
            System.out.println("❌ Создание пользователя: ОШИБКА - " + e.getMessage());
        }
    }

    private static void testTransactionCreation() {
        try {
            Transaction transaction = new Transaction("Food", 100.0, TransactionType.EXPENSE, "Lunch");
            System.out.println("✅ Создание транзакции: УСПЕХ");
        } catch (Exception e) {
            System.out.println("❌ Создание транзакции: ОШИБКА - " + e.getMessage());
        }
    }

    private static void testWalletOperations() {
        try {
            User user = new User("test", "pass");
            double balance = user.getWallet().getBalance();
            System.out.println("✅ Операции с кошельком: УСПЕХ (баланс: " + balance + ")");
        } catch (Exception e) {
            System.out.println("❌ Операции с кошельком: ОШИБКА - " + e.getMessage());
        }
    }
}