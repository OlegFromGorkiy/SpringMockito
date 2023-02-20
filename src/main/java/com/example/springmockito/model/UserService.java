package com.example.springmockito.model;

import com.example.springmockito.exceptions.UserNonUniqueException;

import java.util.List;
import java.util.stream.Collectors;


public class UserService {

        public static void main(String[] args) {
            UserService userService = new UserService(new UserRepository());
            userService.addNewUser("user", "password");
            System.out.println(userService.userCheck("user", "password"));
            System.out.println(userService.userCheck("user", "user"));
        }

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<String> getAllLogins() {
        return repo.getAllUsers().stream()
                .map(User::getLogin)
                .collect(Collectors.toList());
    }

    public void addNewUser(String login, String password) {
        if (login == null || login.isBlank()) throw new IllegalArgumentException("Bad login");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Bad password");
        if (getAllLogins().contains(login)) throw new UserNonUniqueException();

        repo.addUser(new User(login, password));
    }

    public boolean userCheck(String login, String password) {
        return repo.getUser(login, password).isPresent();
    }
}
