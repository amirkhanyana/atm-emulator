package com.example.atm.atmemulator.exception;

public class InsufficientFundsForTransactionException extends RuntimeException {
    private final long fromAccountId;
    private final long toAccountId;
    private final long amount;
    private final long balance;

    public InsufficientFundsForTransactionException(long fromAccountId, long toAccountId, long amount, long balance) {
        super();
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.balance = balance;
    }

    public long getFromAccountId() {
        return fromAccountId;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public long getAmount() {
        return amount;
    }

    public long getBalance() {
        return balance;
    }
}
