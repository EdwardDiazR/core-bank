package com.example.nuevo_core.account.controller;

import com.example.nuevo_core.account.dto.CreateAccountDTO;
import com.example.nuevo_core.account.dto.responsesDto.CreateAccountResponse;
import com.example.nuevo_core.account.interfaces.IAccountService;
import com.example.nuevo_core.account.entity.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/account")
public class AccountController {

    private final IAccountService _accountService;

    public AccountController(IAccountService accountService) {
        _accountService = accountService;
    }

    @GetMapping("{accountNumber}")
    public ResponseEntity<Account> getAccountByAccountNumber(@PathVariable String accountNumber) {
        Account account = _accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    @GetMapping("my-accounts")
    public ResponseEntity<List<Account>> getAccountsByCustomerId(@RequestParam Long customerId) {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("check-balance")
    public ResponseEntity<BigDecimal> checkAccountBalance(@RequestParam Long accountNumber) {
        BigDecimal accountBalance = _accountService.checkBalanceByAccountId(accountNumber);
        return ResponseEntity.ok(accountBalance);
    }

    @PostMapping()
    public ResponseEntity<CreateAccountResponse> createAccount(@RequestBody CreateAccountDTO accountDTO) {
        Account account = _accountService.createAccount(accountDTO);

        CreateAccountResponse response = new CreateAccountResponse(account,
                "Cuenta No." + account.getFinancialProduct().getProductNumber() + " creada exitosamente");

        return ResponseEntity.created(URI.create("")).body(response);
    }

    @PostMapping("reactivate-account")
    public ResponseEntity<String> reactivateAccount(@RequestParam Long accountNumber) {
        //todo: logic to reactivate account
        return ResponseEntity.ok("Account reactivated");
    }

    @PostMapping("hold-amount")
    public ResponseEntity<?> holdAmount(@RequestParam Long accountNumber,
                                        @RequestParam BigDecimal amountToHold) {
        return ResponseEntity.ok("Hold placed");
    }

    @PostMapping("remove-hold")
    public ResponseEntity<?> removeHold(@RequestParam Long accountNumber,
                                        @RequestParam Long holdId) {
        return ResponseEntity.ok("Hold removed");
    }

    @PostMapping("close-account")
    public ResponseEntity<String> closeAccount(@RequestParam Long accountNumber,
                                               @RequestParam String reasonCode) {

        return ResponseEntity.ok("Account closed");
    }
}
