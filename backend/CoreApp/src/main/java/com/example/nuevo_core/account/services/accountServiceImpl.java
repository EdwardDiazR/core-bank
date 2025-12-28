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
import com.example.nuevo_core.transaction.constants.TransactionCategory;
import com.example.nuevo_core.transaction.constants.TransactionStatus;
import com.example.nuevo_core.transaction.constants.TransactionType;
import com.example.nuevo_core.transaction.dto.CreateAccountTxDto;
import com.example.nuevo_core.transaction.dto.CreateTransactionDto;
import com.example.nuevo_core.transaction.interfaces.ITransactionService;
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
    private ITransactionService _transactionService;

    public accountServiceImpl(FinancialProductService financialProductService,
                              AccountRepository accountRepository,
                              ITransactionService transactionService) {
        _financialProductService = financialProductService;
        _accountRepository = accountRepository;
        _transactionService = transactionService;
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
                .orElseThrow(() -> new RuntimeException("Cuenta no existe"));
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
                               CreateAccountTxDto transactionDto
    ) {
        Account account = new Account();

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Cuenta inactiva");
        }

        return applyDebitFromAccount(account, amount, transactionDto);
    }


    public void withdrawMobileCash(MobileCashDto dto) {

    }

    @Transactional
    private BigDecimal applyDebitFromAccount(Account account,
                                             BigDecimal amount,
                                             CreateAccountTxDto transactionDto) {

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

        _transactionService.createTransaction(
                new CreateTransactionDto(
                        transactionDto.description(),
                        transactionDto.amount(),
                        transactionDto.type(),
                        transactionDto.channel(),
                        transactionDto.status(),
                        transactionDto.category(),
                        transactionDto.financialProductId(),
                        transactionDto.referenceId(),
                        newBalance,
                        transactionDto.currency())
        );
        return amount;
    }

    @Transactional
    private void applyCreditToAccount(Account account, BigDecimal amount, CreateAccountTxDto transactionDto) {
        BigDecimal newBalance = BigDecimal.ONE;
        account.setTotalBalance(account.getTotalBalance().add(amount));

        _transactionService.createTransaction(
                new CreateTransactionDto(
                        transactionDto.description(),
                        transactionDto.amount(),
                        transactionDto.type(),
                        transactionDto.channel(),
                        transactionDto.status(),
                        transactionDto.category(),
                        transactionDto.financialProductId(),
                        transactionDto.referenceId(),
                        newBalance,
                        account.getCurrency().toString())
        );

        _accountRepository.save(account);
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

    @Transactional
    public void transfer(TransferDto transferDto) {

        Long referenceId = _transactionService.generateReferenceId();
        Account fromAccount = new Account();
        Account toAccount = new Account();

        boolean canDeposit = toAccount.isAllowCredits();

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

        CreateAccountTxDto debitTxDto = new CreateAccountTxDto(debitDescription,
                transferDto.amount(),
                TransactionType.DEBIT,
                "channel",
                TransactionStatus.COMPLETED,
                TransactionCategory.TRANSFER,
                fromAccount.getFinancialProduct(),
                referenceId,
                fromAccount.getCurrency().toString());

        BigDecimal transferAmountBalance = applyDebitFromAccount(fromAccount, transferDto.amount(), debitTxDto);

        //credit
        String creditDescription = transferDto.desc() != null ?
                transferDto.desc().toUpperCase() : "Transf desde cta " + transferDto.fromAccountId();


        CreateAccountTxDto creditTxDto = new CreateAccountTxDto(creditDescription,
                transferAmountBalance,
                TransactionType.CREDIT,
                "channel",
                TransactionStatus.COMPLETED,
                TransactionCategory.TRANSFER,
                toAccount.getFinancialProduct(),
                referenceId,
                fromAccount.getCurrency().toString());
        applyCreditToAccount(toAccount, transferAmountBalance, creditTxDto);

    }


}
