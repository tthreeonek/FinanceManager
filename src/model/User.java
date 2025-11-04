package com.finance.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private Wallet wallet;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.wallet = new Wallet(username);
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Wallet getWallet() { return wallet; }

    @Override
    public String toString() {
        return String.format("User{username='%s', wallet=%s}", username, wallet);
    }
}