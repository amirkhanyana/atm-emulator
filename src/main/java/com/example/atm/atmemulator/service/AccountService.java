package com.example.atm.atmemulator.service;

import com.example.atm.atmemulator.entity.Account;
import com.example.atm.atmemulator.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
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
        var optionalUser = userService.getUserById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("No user exists with id " + userId);
        }
        var account = new Account();
        account.setUser(optionalUser.get());
        return accountRepository.save(account);
    }

    public Optional<Account> getAccountById(long accountId) {
        return accountRepository.findById(accountId);
    }

    public void setAccountBalance(long accountId, @Min(value = 0) long newBalance) {

        var optionalAccount = getAccountById(accountId);
        if (optionalAccount.isEmpty()) {
            throw new IllegalArgumentException("No account exists with id " + accountId);
        }
        var account = optionalAccount.get();
        account.setBalance(newBalance);
        accountRepository.save(account);
    }
}
