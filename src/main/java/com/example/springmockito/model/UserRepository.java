package com.example.springmockito.model;


import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;


public class UserRepository {
    private final Collection<User> users = new HashSet<>();


    public Collection<User> getAllUsers() {
        return users;
    }

    public Optional<User> getUser(String login) {
        return users.stream()
                .filter(u -> u.getLogin().equals(login))
                .findAny();
    }

    public Optional<User> getUser(String login, String password) {
        return users.stream()
                .filter(u -> u.getLogin().equals(login) && u.getPassword().equals(password))
                .findAny();
    }

    public void addUser(User user) {
        users.add(user);
    }
}
