package com.example.nuevo_core.account.interfaces;

import com.example.nuevo_core.account.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface IAccountService {
    void createAccount();
    Account getAccountById();
    List<Account> getAccountsByCustomerId();
    BigDecimal getAccountBalanceByAccountId(Long accountId);
    BigDecimal withdrawAmountFromAccount(Long accountId,BigDecimal amount,String description);
}
