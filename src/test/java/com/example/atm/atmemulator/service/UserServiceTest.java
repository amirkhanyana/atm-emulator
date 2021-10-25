package com.example.atm.atmemulator.service;

import com.example.atm.atmemulator.entity.Account;
import com.example.atm.atmemulator.entity.User;
import com.example.atm.atmemulator.repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.ConstraintViolationException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
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
        Mockito.when(userRepository.save(any(User.class))).thenReturn(expected);
        Mockito.when(accountService.createAccountForUser(userId)).thenReturn(new Account());
        User user = userService.createUser(username);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(username, user.getUsername());
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
}
