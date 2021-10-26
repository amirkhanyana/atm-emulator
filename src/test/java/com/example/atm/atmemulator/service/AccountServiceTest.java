package com.example.atm.atmemulator.service;

import com.example.atm.atmemulator.entity.Account;
import com.example.atm.atmemulator.entity.User;
import com.example.atm.atmemulator.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
public class AccountServiceTest {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Test
    public void whenCreateAccountThenReturnCreated() {
        Account expected = new Account();
        expected.setId(1);
        Mockito.when(accountRepository.save(any())).thenReturn(expected);
        Account account = accountService.createAccount();
        Assertions.assertNotNull(account);
        Assertions.assertEquals(expected.getId(), account.getId());
    }

    @Test
    public void whenGetExistingAccountByIdThenReturn() {
        long accountId = 1;
        Account expected = new Account();
        expected.setId(accountId);
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(expected));
        Optional<Account> account = accountService.getAccountById(accountId);
        Assertions.assertTrue(account.isPresent());
        Assertions.assertEquals(expected.getId(), account.get().getId());
    }

    @Test
    public void whenGetNonExistingAccountByIdThenReturnEmpty() {
        long accountId = 1;
        Account expected = new Account();
        expected.setId(accountId);
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        Optional<Account> account = accountService.getAccountById(accountId);
        Assertions.assertTrue(account.isEmpty());
    }

    @Test
    public void whenSetAccountBalanceThenOK() {
        long accountId = 1;
        long balance = 1;
        Account expected = new Account();
        expected.setId(accountId);
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(expected));
        accountService.setAccountBalance(accountId, balance);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -100})
    public void whenSetAccountToInvalidBalanceThenThrowException(long balance) {
        long accountId = 1;
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> accountService.setAccountBalance(accountId, balance));
    }

    @Test
    public void whenSetNonExistingAccountBalanceThenThrowException() {
        long accountId = 1;
        long balance = 1;
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> accountService.setAccountBalance(accountId, balance));
    }
}
