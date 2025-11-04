package com.finance.storage;

import com.finance.model.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStorage {
    private Map<String, User> users;

    public DataStorage() {
        this.users = new ConcurrentHashMap<>();
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public Map<String, User> getAllUsers() {
        return new ConcurrentHashMap<>(users);
    }
}