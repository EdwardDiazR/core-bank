package com.example.nuevo_core.account.services;

import com.example.nuevo_core.account.interfaces.IAccountService;
import com.example.nuevo_core.account.model.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class accountService implements IAccountService {

    public accountService() {
    }

    public void createAccount() {
    }

    public Account getAccountById() {
        return new Account();
    }

    public List<Account> getAccountsByCustomerId() {
        return new ArrayList<>();
    }

    public BigDecimal getAccountBalanceByAccountId(Long accountId) {
        return BigDecimal.ZERO;
    }

    public BigDecimal withdrawAmountFromAccount(Long accountId,
                                                BigDecimal amount,
                                                String description) {
        return BigDecimal.ZERO;
    }

    public void markAccountAsInactive(){}

    public void closeAccount(){}

}
