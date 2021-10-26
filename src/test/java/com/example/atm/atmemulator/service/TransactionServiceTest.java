package com.example.atm.atmemulator.service;

import com.example.atm.atmemulator.entity.Account;
import com.example.atm.atmemulator.entity.Transaction;
import com.example.atm.atmemulator.exception.InsufficientFundsForTransactionException;
import com.example.atm.atmemulator.repository.TransactionRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@SpringBootTest(
        properties = {
                InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
                ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
        }
)
class TransactionServiceTest {
    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -100})
    void whenPerformTransactionWithIllegalAmountThenThrowException(long amount) {
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> transactionService.performTransaction(1, 1, amount));
    }

    @Test
    void whenPerformTransactionWithNonExistingSenderAccountThenThrowException() {
        Mockito.when(accountService.getAccountById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> transactionService.performTransaction(1, 2, 1));
    }

    @Test
    void whenPerformTransactionWithNonExistingReceiverAccountThenThrowException() {
        Mockito.when(accountService.getAccountById(1)).thenReturn(Optional.of(new Account()));
        Mockito.when(accountService.getAccountById(2)).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> transactionService.performTransaction(1, 2, 1));
    }

    @Test
    void whenPerformTransactionWithInsufficientFundsThenThrowException() {
        long senderId = 1;
        long senderBalance = 10;
        long amount = 11;
        long receiverId = 2;
        Account sender = new Account();
        sender.setId(senderId);
        sender.setBalance(senderBalance);
        Account receiver = new Account();
        receiver.setId(receiverId);
        Mockito.when(accountService.getAccountById(senderId)).thenReturn(Optional.of(sender));
        Mockito.when(accountService.getAccountById(receiverId)).thenReturn(Optional.of(receiver));
        Assertions.assertThrows(InsufficientFundsForTransactionException.class,
                () -> transactionService.performTransaction(senderId, receiverId, amount));
        // TODO: Mockito is not the best lib for testing exception details, use alternative test stack
    }

    @Test
    void whenPerformValidTransactionThenReturnTransaction() {
        long senderId = 1;
        long receiverId = 2;
        long senderBalance = 10;
        long amount = 9;
        Account sender = new Account();
        sender.setId(senderId);
        sender.setBalance(senderBalance);
        Account receiver = new Account();
        receiver.setId(receiverId);
        Transaction expected = new Transaction();
        expected.setAmount(amount);
        expected.setFromAccount(sender);
        expected.setToAccount(receiver);
        Mockito.when(accountService.getAccountById(senderId)).thenReturn(Optional.of(sender));
        Mockito.when(accountService.getAccountById(receiverId)).thenReturn(Optional.of(receiver));
        Mockito.when(transactionRepository.save(any())).thenReturn(expected);
        Transaction transaction = transactionService.performTransaction(senderId, receiverId, amount);
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertNotNull(transaction.getFromAccount());
        assertNotNull(transaction.getToAccount());
        assertEquals(senderId, transaction.getFromAccount().getId());
        assertEquals(receiverId, transaction.getToAccount().getId());
    }
}