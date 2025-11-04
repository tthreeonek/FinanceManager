package com.finance.storage;

import com.finance.model.User;
import com.finance.model.Wallet;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileStorage {
    private static final String DATA_DIR = "data/users/";

    public FileStorage() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Ошибка создания директории: " + e.getMessage());
        }
    }

    public void saveUserData(User user) {
        String filename = DATA_DIR + user.getUsername() + ".dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(user.getWallet());
        } catch (IOException e) {
            System.err.println("Ошибка сохранения данных пользователя " + user.getUsername() + ": " + e.getMessage());
        }
    }

    public Wallet loadUserData(String username) {
        String filename = DATA_DIR + username + ".dat";
        File file = new File(filename);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (Wallet) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка загрузки данных пользователя " + username + ": " + e.getMessage());
            return null;
        }
    }
}