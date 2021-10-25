package com.example.atm.atmemulator.service;

import com.example.atm.atmemulator.entity.Transaction;
import com.example.atm.atmemulator.exception.InsufficientFundsForTransactionException;
import com.example.atm.atmemulator.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

@Service
@Validated
public class TransactionService {
    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public Transaction performTransaction(long fromAccountId, long toAccountId, @Min(value = 1) long amount) {
        var optionalFromAccount = accountService.getAccountById(fromAccountId);
        if (optionalFromAccount.isEmpty()) {
            throwNoAccountWithId(fromAccountId);
        }
        var optionalToAccount = accountService.getAccountById(toAccountId);
        if (optionalToAccount.isEmpty()) {
            throwNoAccountWithId(toAccountId);
        }
        var fromAccount = optionalFromAccount.get();
        if (fromAccount.getBalance() < amount) {
            throw new InsufficientFundsForTransactionException(fromAccountId,
                    toAccountId,
                    amount,
                    fromAccount.getBalance());
        }
        var senderNewBalance = fromAccount.getBalance() - amount;
        accountService.setAccountBalance(fromAccountId, senderNewBalance);

        var toAccount = optionalToAccount.get();
        var recipientNewBalance = toAccount.getBalance() + amount;
        accountService.setAccountBalance(toAccountId, recipientNewBalance);

        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        return transactionRepository.save(transaction);
    }

    private void throwNoAccountWithId(long accountId) {
        throw new IllegalArgumentException("No account exists with id " + accountId); // TODO: throw custom exception
    }
}
