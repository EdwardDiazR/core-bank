package com.example.nuevo_core.account.repository;

import com.example.nuevo_core.account.entity.Account;
import com.example.nuevo_core.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {

    Optional<Account> findByFinancialProduct_ProductNumber(String accountNumber);
}
