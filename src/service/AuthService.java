package com.finance.service;

import com.finance.model.User;
import com.finance.storage.DataStorage;

public class AuthService {
    private DataStorage dataStorage;
    private User currentUser;

    public AuthService(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    public boolean register(String username, String password) {
        ValidationService.validateUsername(username);
        ValidationService.validatePassword(password);

        if (dataStorage.userExists(username)) {
            return false;
        }

        User newUser = new User(username, password);
        dataStorage.addUser(newUser);
        return true;
    }

    public boolean login(String username, String password) {
        ValidationService.validateUsername(username);
        ValidationService.validatePassword(password);

        User user = dataStorage.getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}