package com.example.atm.atmemulator.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    private User user;

    private long balance;

    @OneToMany(
            targetEntity = Transaction.class,
            mappedBy = "fromAccount",
            fetch = FetchType.LAZY
    )
    private List<Transaction> transactionsSent;

    @OneToMany(
            targetEntity = Transaction.class,
            mappedBy = "toAccount",
            fetch = FetchType.LAZY
    )
    private List<Transaction> transactionsReceived;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public List<Transaction> getTransactionsSent() {
        return transactionsSent;
    }

    public void setTransactionsSent(List<Transaction> transactionsSent) {
        this.transactionsSent = transactionsSent;
    }

    public List<Transaction> getTransactionsReceived() {
        return transactionsReceived;
    }

    public void setTransactionsReceived(List<Transaction> transactionsReceived) {
        this.transactionsReceived = transactionsReceived;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
