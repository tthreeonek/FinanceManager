package com.finance.service;

import java.util.regex.Pattern;

public class ValidationService {

    public static void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Логин не может быть пустым");
        }
        if (username.length() < 3) {
            throw new IllegalArgumentException("Логин должен содержать минимум 3 символа");
        }
        if (!Pattern.matches("^[a-zA-Z0-9_]+$", username)) {
            throw new IllegalArgumentException("Логин может содержать только буквы, цифры и подчёркивания");
        }
    }

    public static void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }
        if (password.length() < 4) {
            throw new IllegalArgumentException("Пароль должен содержать минимум 4 символа");
        }
    }

    public static void validateCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Категория не может быть пустой");
        }
        if (category.length() > 50) {
            throw new IllegalArgumentException("Название категории слишком длинное");
        }
    }

    public static void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }
        if (amount > 1_000_000_000) {
            throw new IllegalArgumentException("Сумма слишком большая");
        }
    }

    public static void validateDescription(String description) {
        if (description != null && description.length() > 200) {
            throw new IllegalArgumentException("Описание слишком длинное");
        }
    }
}