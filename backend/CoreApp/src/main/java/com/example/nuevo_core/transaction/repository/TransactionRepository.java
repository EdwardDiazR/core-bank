package com.example.nuevo_core.transaction.repository;

import com.example.nuevo_core.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
