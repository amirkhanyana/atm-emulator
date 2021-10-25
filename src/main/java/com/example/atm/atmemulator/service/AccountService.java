package com.example.atm.atmemulator.service;

import com.example.atm.atmemulator.entity.Account;
import com.example.atm.atmemulator.entity.User;
import com.example.atm.atmemulator.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Lazy
    @Autowired
    private UserService userService;

    public Account createAccountForUser(long userId) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("No user exists with id " + userId);
        }
        Account account = new Account();
        account.setUser(user.get());
        return accountRepository.save(account);
    }
}
