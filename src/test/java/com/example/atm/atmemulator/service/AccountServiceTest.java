package com.example.atm.atmemulator.service;

import com.example.atm.atmemulator.entity.Account;
import com.example.atm.atmemulator.entity.User;
import com.example.atm.atmemulator.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


@SpringBootTest
public class AccountServiceTest {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Test
    public void whenCreateAccountThenReturnCreated() {
        long userId = 0;
        User expectedUser = new User();
        expectedUser.setId(userId);
        Account expected = new Account();
        expected.setUser(expectedUser);
        Mockito.when(userService.getUserById(userId)).thenReturn(Optional.of(expectedUser));
        Mockito.when(accountRepository.save(any())).thenReturn(expected);
        Account account = accountService.createAccountForUser(userId);
        Assertions.assertNotNull(account);
        User user = account.getUser();
        Assertions.assertNotNull(user);
        Assertions.assertEquals(userId, user.getId());
    }

    @Test
    public void whenCreateAccountForNonExistingUserThenThrowException() {
        long userId = 0;
        User expectedUser = new User();
        expectedUser.setId(userId);
        Account expected = new Account();
        expected.setUser(expectedUser);
        Mockito.when(userService.getUserById(userId)).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> accountService.createAccountForUser(userId));
    }
}
