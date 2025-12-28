package com.example.nuevo_core.account.interfaces;

import com.example.nuevo_core.account.dto.CreateAccountDTO;
import com.example.nuevo_core.account.entity.Account;
import com.example.nuevo_core.transaction.dto.CreateAccountTxDto;
import com.example.nuevo_core.transaction.dto.CreateTransactionDto;

import java.math.BigDecimal;
import java.util.List;

public interface IAccountService {
    Account createAccount(CreateAccountDTO dto);
    Account getAccountById(Long accountNumber);
    Account getAccountByNumber(String accountNumber);
    List<Account> getAccountsByCustomerId();
    BigDecimal checkBalanceByAccountId(Long accountId);
    BigDecimal withdraw(Long accountId, BigDecimal amount, CreateAccountTxDto transactionDto);
}
