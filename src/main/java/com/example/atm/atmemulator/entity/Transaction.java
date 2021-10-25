package com.example.atm.atmemulator.entity;

import javax.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="from_id")
    private Account fromAccount;

    @ManyToOne(optional = false)
    @JoinColumn(name="to_id")
    private Account toAccount;

    private long amount;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account from) {
        this.fromAccount = from;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account to) {
        this.toAccount = to;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
