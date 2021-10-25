package com.example.atm.atmemulator.service;

import com.example.atm.atmemulator.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void whenCreateUserThenReturnCreated() {
        String username = "username";
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
}
