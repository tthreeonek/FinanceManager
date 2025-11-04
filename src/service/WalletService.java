package com.finance.service;

import com.finance.model.User;
import com.finance.model.Transaction;
import com.finance.model.TransactionType;
import com.finance.storage.DataStorage;

public class WalletService {
    private DataStorage dataStorage;

    public WalletService(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    public boolean transferMoney(String fromUsername, String toUsername, double amount, String description) {
        ValidationService.validateAmount(amount);
        ValidationService.validateDescription(description);

        if (fromUsername.equals(toUsername)) {
            throw new IllegalArgumentException("Нельзя переводить самому себе");
        }

        User fromUser = dataStorage.getUser(fromUsername);
        User toUser = dataStorage.getUser(toUsername);

        if (fromUser == null || toUser == null) {
            return false;
        }

        if (fromUser.getWallet().getBalance() < amount) {
            return false;
        }

        // Добавляем расход отправителю
        Transaction expense = new Transaction("Перевод", amount, TransactionType.EXPENSE,
                "Перевод пользователю: " + toUsername + ". " + description);
        fromUser.getWallet().addTransaction(expense);

        // Добавляем доход получателю
        Transaction income = new Transaction("Перевод", amount, TransactionType.INCOME,
                "Перевод от пользователя: " + fromUsername + ". " + description);
        toUser.getWallet().addTransaction(income);

        return true;
    }
}