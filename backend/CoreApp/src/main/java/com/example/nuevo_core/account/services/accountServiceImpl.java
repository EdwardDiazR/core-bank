package com.example.nuevo_core.account.services;

import com.example.nuevo_core.account.constants.AccountStatus;
import com.example.nuevo_core.account.dto.CreateAccountDTO;
import com.example.nuevo_core.account.dto.TransferDto;
import com.example.nuevo_core.account.exceptions.InsufficientFundsException;
import com.example.nuevo_core.account.interfaces.IAccountService;
import com.example.nuevo_core.account.entity.Account;
import com.example.nuevo_core.account.repository.AccountRepository;
import com.example.nuevo_core.financialProduct.constants.ProductType;
import com.example.nuevo_core.financialProduct.dto.CreateFinancialProductDTO;
import com.example.nuevo_core.financialProduct.entity.FinancialProduct;
import com.example.nuevo_core.financialProduct.interfaces.FinancialProductService;
import com.example.nuevo_core.mobileCash.dto.MobileCashDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class accountServiceImpl implements IAccountService {
    private final FinancialProductService _financialProductService;
    private final AccountRepository _accountRepository;

    public accountServiceImpl(FinancialProductService financialProductService,
                              AccountRepository accountRepository) {
        _financialProductService = financialProductService;
        _accountRepository = accountRepository;
    }

    @Transactional
    public Account createAccount(CreateAccountDTO accountDTO) {
        System.out.println(accountDTO.relatives());
        System.out.println(accountDTO.currency());

        FinancialProduct financialProduct = _financialProductService.createFinancialProduct(
                new CreateFinancialProductDTO(ProductType.ACCOUNT,
                accountDTO.relatives(),
                accountDTO.signType()));


        Account account = Account.builder()
                .financialProduct(financialProduct)
                .status(AccountStatus.ACTIVE)
                .currency(accountDTO.currency())
                .totalBalance(BigDecimal.ZERO)
                .inTransitAmount(BigDecimal.ZERO)
                .holdAmount(BigDecimal.ZERO)
                .availableBalance(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .lastDepositDate(null)
                .lastActivityDate(null)
                .hasFirstDeposit(false)
                .allowDebits(true)
                .allowCredits(true)
                .build();

        _accountRepository.save(account);

        return account;


    }

    public Account getAccountById(Long accountNumber) {
        return new Account();
    }

    public Account getAccountByNumber(String accountNumber) {
        return _accountRepository.findByFinancialProduct_ProductNumber(accountNumber)
                .orElseThrow(()->new RuntimeException("Cuenta no existe"));
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
