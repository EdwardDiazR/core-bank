package com.example.nuevo_core.account.services;

import com.example.nuevo_core.account.constants.AccountStatus;
import com.example.nuevo_core.account.dto.TransferDto;
import com.example.nuevo_core.account.exceptions.InsufficientFundsException;
import com.example.nuevo_core.account.interfaces.IAccountService;
import com.example.nuevo_core.account.entity.Account;
import com.example.nuevo_core.mobileCash.dto.MobileCashDto;
import jakarta.transaction.Transactional;
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

    public Account getAccountById(Long accountNumber) {
        return new Account();
    }

    public List<Account> getAccountsByCustomerId() {
        return new ArrayList<>();
    }

    public BigDecimal checkBalanceByAccountId(Long accountId) {
        Account account = new Account();
        return calculateAvailableBalance(account.getTotalBalance(), account.getInTransitAmount(), account.getHoldAmount());
    }


    public void deposit() {
    }

    @Transactional
    public BigDecimal withdraw(Long accountId,
                               BigDecimal amount,
                               String description
    ) {
        Account account = new Account();

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Cuenta inactiva");
        }

        return applyDebitFromAccount(account, amount, description);
    }


    public void withdrawMobileCash(MobileCashDto dto) {

    }

    private BigDecimal applyDebitFromAccount(Account account, BigDecimal amount, String description) {

        BigDecimal availableBalance = calculateAvailableBalance(account.getTotalBalance(),
                account.getInTransitAmount(),
                account.getHoldAmount());


        if (!accountHasSufficientFunds(availableBalance, amount)) {
            throw new InsufficientFundsException("Cuenta no posee balance suficiente");
        }

        BigDecimal newBalance = account.getAvailableBalance().subtract(amount);
        account.setTotalBalance(newBalance);

        //todo:save
        //todo:create transaction with amount.negate()
        return amount;
    }

    private void applyCreditToAccount(Account account, BigDecimal amount, String description) {
        account.setTotalBalance(account.getTotalBalance().add(amount));

        //todo: create transaction
        //todo: save in db
    }

    public boolean accountHasSufficientFunds(BigDecimal accountBalance, BigDecimal withdrawAmount) {
        return accountBalance.compareTo(withdrawAmount) >= 0;
    }

    public BigDecimal calculateAvailableBalance(BigDecimal totalBalance, BigDecimal inTransitBalance, BigDecimal blockedBalance) {
        return totalBalance
                .subtract(inTransitBalance)
                .subtract(blockedBalance);
    }

    public void markAccountAsInactive() {
    }

    public void closeAccount() {
    }

    public void transfer(TransferDto transferDto) {
        Account fromAccount = new Account();
        Account toAccount = new Account();
        boolean canDeposit = false;

        if (toAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Cuenta inactiva");
        }

        if (canDeposit) {
            throw new RuntimeException("Esta cuenta no puede recibir deposito");
        }
        //debit
        String debitDescription = transferDto.desc() != null ?
                transferDto.desc().toUpperCase()
                : "Transf a cta " + transferDto.toAccountId();
        BigDecimal transferAmountBalance = applyDebitFromAccount(fromAccount, transferDto.amount(), debitDescription);

        //credit
        String creditDescription = transferDto.desc() != null ? transferDto.desc().toUpperCase() : "Transf desde cta " + transferDto.fromAccountId();
        applyCreditToAccount(toAccount, transferAmountBalance, creditDescription);

    }


}
