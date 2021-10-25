package com.example.atm.atmemulator.service;

import com.example.atm.atmemulator.entity.Account;
import com.example.atm.atmemulator.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AccountService {
    public Account creatAccountForUser(long userId) {
        Account account = new Account();
        User user = new User();
        user.setId(userId);
        account.setUser(user);
        return account;
    }
}
