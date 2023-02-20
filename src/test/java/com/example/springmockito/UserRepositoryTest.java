package com.example.springmockito;

import com.example.springmockito.model.User;
import com.example.springmockito.model.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;

public class UserRepositoryTest {

    private UserRepository repo;

    @BeforeEach
    public void setUp() {
        repo = new UserRepository();
    }

    @Test
    public void returnEmptyCollection() {
        //Получение пустого списка пользователей → должен возвращаться пустой список.
        Assertions.assertTrue(repo.getAllUsers().isEmpty());
    }

    @Test
    public void checkUsersCollection() {
        //Получение списка пользователей при изначально заполненном сервисе → должны возвращаться те же самые пользователи которых добавляли.
        Collection<User> users = generateUsers();
        for (User u : users) {
            repo.addUser(u);
        }
        Assertions.assertEquals(users, repo.getAllUsers());
    }

    @Test
    public void checkCorrectLoginFind() {
        // Поиск пользователя по логину → в случае если такой пользователь есть.
        Collection<User> users = generateUsers();
        for (User u : users) {
            repo.addUser(u);
        }
        Assertions.assertTrue(repo.getUser("User_1").isPresent());
    }

    @Test
    public void checkIncorrectLoginFind() {
        // Поиск пользователя по логину → в случае когда такого пользователя нет.
        Collection<User> users = generateUsers();
        for (User u : users) {
            repo.addUser(u);
        }
        Assertions.assertTrue(repo.getUser("User_5").isEmpty());
    }

    @Test
    public void checkCorrectLoginAndPasswordFind() {
        // Поиск пользователя по логину → в случае если такой пользователь есть.
        Collection<User> users = generateUsers();
        for (User u : users) {
            repo.addUser(u);
        }
        Assertions.assertTrue(repo.getUser("User_1", "1UserPass1").isPresent());
    }

    @Test
    public void checkBadLoginAndCorrectPasswordFind() {
        // Поиск пользователя по логину → в случае если такой пользователь есть.
        Collection<User> users = generateUsers();
        for (User u : users) {
            repo.addUser(u);
        }
        Assertions.assertTrue(repo.getUser("User_5", "1UserPass1").isEmpty());
    }


    @Test
    public void checkCorrectLoginAndBadPasswordFind() {
        // Поиск пользователя по логину → в случае если такой пользователь есть.
        UserRepository repo = new UserRepository();
        Collection<User> users = generateUsers();
        for (User u : users) {
            repo.addUser(u);
        }
        Assertions.assertTrue(repo.getUser("User_1", "1UserPass2").isEmpty());
    }

    private Collection<User> generateUsers() {
        Collection<User> result = new HashSet<>();
        for (int i = 1; i < 5; i++) {
            result.add(new User("User_" + i, i + "UserPass" + i));
        }
        return result;
    }
}
