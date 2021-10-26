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

    public Account createAccount() {
        var account = new Account();
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
