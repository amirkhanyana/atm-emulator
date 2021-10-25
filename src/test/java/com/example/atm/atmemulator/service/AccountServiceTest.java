package com.example.atm.atmemulator.service;

import com.example.atm.atmemulator.entity.Account;
import com.example.atm.atmemulator.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void whenCreateAccountThenReturnCreated() {
        long userId = 0;
        Account account = accountService.creatAccountForUser(userId);
        Assertions.assertNotNull(account);
        User user = account.getUser();
        Assertions.assertNotNull(user);
        Assertions.assertEquals(userId, user.getId());
    }
}
