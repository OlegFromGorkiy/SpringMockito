package com.example.springmockito;

import com.example.springmockito.exceptions.UserNonUniqueException;
import com.example.springmockito.model.User;
import com.example.springmockito.model.UserRepository;
import com.example.springmockito.model.UserService;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    public static Stream<Arguments> firstParameters() {
        return Stream.of(
                Arguments.of(null, "password"),
                Arguments.of("    ", "password"),
                Arguments.of("login", null),
                Arguments.of("login", "    ")
        );
    }

    public static Stream<Arguments> secondParameters() {
        return Stream.of(
                Arguments.of("User_1", "1UserPass1"),
                Arguments.of("User_2", "2UserPass2"),
                Arguments.of("User_3", "3UserPass3")
        );
    }

    @Mock
    private UserRepository repo;

    @InjectMocks
    private UserService userService;

    @Test
    public void getAllLoginsNoUser() {
        assertNotNull(repo);
        when(repo.getAllUsers()).thenReturn(new HashSet<>());
        assertTrue(userService.getAllLogins().isEmpty());
    }

    @Test
    public void getAllLoginsWithUser() {
        assertNotNull(repo);
        when(repo.getAllUsers()).thenReturn(generateUsers());
        assertFalse(userService.getAllLogins().isEmpty());
    }

    @Test
    public void getAllLoginsWithUserAndCheckLogins() {
        assertNotNull(repo);
        when(repo.getAllUsers()).thenReturn(generateUsers());
        assertEquals(userService.getAllLogins(), generateLogins());
    }

    @ParameterizedTest
    @MethodSource("firstParameters")
    public void addNewUserIllegalArgument(String login, String password) {
        assertNotNull(repo);
        assertThrows(IllegalArgumentException.class,
                () -> userService.addNewUser(login, password));
    }

    @Test
    public void addNewUserUserNonUnique() {
        assertNotNull(repo);
        when(repo.getAllUsers()).thenReturn(generateUsers()); //c моком, по мне корявенько, но тест проходит.
        //userService.addNewUser("User_1", "password"); //так по моему лучше и нагляднее, но тест не проходит. Почему?
        assertThrows(UserNonUniqueException.class,
                () -> userService.addNewUser("User_1", "password"));
    }

    @Test
    public void addNewUserUserAdded() {
        assertNotNull(repo);
        when(repo.getAllUsers()).thenReturn(generateUsers());
        assertDoesNotThrow(() -> userService.addNewUser("User", "Password"));
        verify(repo, times(1)).addUser(any());
    }

    @ParameterizedTest
    @MethodSource("secondParameters")
    public void userCheckParameterizedTest(String login, String password) {
        assertNotNull(repo);
        when(repo.getUser(login, password)).thenReturn(Optional.of(new User(login, password)));
        boolean condition = userService.userCheck(login, password);
        verify(repo, times(1)).getUser(login, password);
        assertTrue(condition);
    }

    private Collection<User> generateUsers() {
        Collection<User> result = new HashSet<>();
        for (int i = 1; i < 5; i++) {
            result.add(new User("User_" + i, i + "UserPass" + i));
        }
        return result;
    }

    private List<String> generateLogins() {
        return generateUsers().stream()
                .map(User::getLogin)
                .collect(Collectors.toList());
    }
}
