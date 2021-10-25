package com.example.atm.atmemulator.repository;

import com.example.atm.atmemulator.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
