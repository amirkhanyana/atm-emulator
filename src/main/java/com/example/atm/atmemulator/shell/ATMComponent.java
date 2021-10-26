package com.example.atm.atmemulator.shell;

import com.example.atm.atmemulator.entity.Account;
import com.example.atm.atmemulator.entity.User;
import com.example.atm.atmemulator.service.AccountService;
import com.example.atm.atmemulator.service.TransactionService;
import com.example.atm.atmemulator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@ShellComponent
public class ATMComponent {
    private Long loggedInUserId = null;
    private Long bankAccountId = null;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @ShellMethod("Login with username")
    public String login(@NotBlank String username) {
        User user = getOrCreateUserByUsername(username);
        loggedInUserId = user.getId();
        return "Hello, " + username + "!\nYour balance is $" + getBalance();
    }

    @ShellMethod("Deposit money to user")
    public String deposit(@Min(value = 1) long amount) {
        var bankAccount = getBankAccount();
        if (bankAccount.getBalance() < amount) {
            return "Bank is out of funds";
        }
        var userAccount = getUser().getAccount();
        transactionService.performTransaction(bankAccount.getId(), userAccount.getId(), amount);
        return "Your balance is $" + getBalance();
    }

    @ShellMethod("Transfer money to user")
    public String transfer(@NotBlank String username, @Min(value = 1) long amount) {
        var userAccount = getUser().getAccount();
        if (userAccount.getBalance() < amount) {
            return "Insufficient funds";
        }
        Account receiverAccount = getOrCreateUserByUsername(username).getAccount();
        transactionService.performTransaction(userAccount.getId(), receiverAccount.getId(), amount);
        return "Transferred $" + amount + " to " + username + "\nYour balance is $" + getBalance();
    }

    @ShellMethod("Logout user")
    public String logout() {
        String out = "Goodbye, " + getUser().getUsername();
        loggedInUserId = null;
        return out;
    }

    @ShellMethodAvailability({"deposit", "logout", "transfer"})
    public Availability loggedInCheck() {
        return loggedInUserId == null
                ? Availability.unavailable("You are not logged in")
                : Availability.available();
    }

    @ShellMethodAvailability({"login"})
    public Availability loggedOutCheck() {
        return loggedInUserId != null
                ? Availability.unavailable("You are logged in")
                : Availability.available();
    }

    private long getBalance() {
        return getUser().getAccount().getBalance();
    }

    private User getUser() {
        return userService.getUserById(loggedInUserId).orElseThrow();
    }

    private User getOrCreateUserByUsername(String username) {
        return userService.getUserByUsername(username)
                .orElseGet(() -> userService.createUser(username));
    }

    private Account getBankAccount() {
        if (bankAccountId == null) {
            Account bankAccount = accountService.createAccount();
            bankAccountId = bankAccount.getId();
            accountService.setAccountBalance(bankAccountId, Long.MAX_VALUE);
        }
        return accountService.getAccountById(bankAccountId).orElseThrow();
    }
}
