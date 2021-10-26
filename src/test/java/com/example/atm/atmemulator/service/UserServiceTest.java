package com.example.atm.atmemulator.service;

import com.example.atm.atmemulator.entity.Account;
import com.example.atm.atmemulator.entity.User;
import com.example.atm.atmemulator.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;

import javax.validation.ConstraintViolationException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


@SpringBootTest(
        properties = {
                InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
                ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
        }
)
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Test
    public void whenCreateUserThenReturnCreated() {
        String username = "username";
        long userId = 1;
        User expected = new User();
        expected.setId(userId);
        expected.setUsername(username);
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        Mockito.when(accountService.createAccount()).thenReturn(new Account());
        Mockito.when(userRepository.save(any(User.class))).thenReturn(expected);
        User user = userService.createUser(username);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(username, user.getUsername());
    }

    @Test
    public void whenCreateUserWithUsedUsernameThenThrowException() {
        String username = "username";
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(username));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    public void whenCreateUserWithInvalidUsernameThenThrowException(String username) {
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> userService.createUser(username));
    }

    @Test
    public void whenGetUserByIdThenReturnFound() {
        long userId = 1;
        User expected = new User();
        expected.setId(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(expected));
        Optional<User> user = userService.getUserById(userId);
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(expected.getId(), user.get().getId());
    }

    @Test
    public void whenGetUserByIdThenReturnEmpty() {
        long userId = 1;
        User expected = new User();
        expected.setId(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());
        Optional<User> user = userService.getUserById(userId);
        Assertions.assertTrue(user.isEmpty());
    }



    @Test
    public void whenGetUserByUsernameThenReturnFound() {
        long userId = 1;
        String username = "username";
        User expected = new User();
        expected.setId(userId);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(expected));
        Optional<User> user = userService.getUserByUsername(username);
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(expected.getId(), user.get().getId());
    }

    @Test
    public void whenGetUserByUsernameThenReturnEmpty() {
        long userId = 1;
        User expected = new User();
        String username = "username";
        expected.setId(userId);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        Optional<User> user = userService.getUserByUsername(username);
        Assertions.assertTrue(user.isEmpty());
    }
}
