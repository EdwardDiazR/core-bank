package com.example.nuevo_core.account.controller;

import com.example.nuevo_core.account.interfaces.IAccountService;
import com.example.nuevo_core.account.model.Account;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/account")
public class AccountController {

    private final IAccountService _accountService;

    public AccountController(IAccountService accountService) {
        _accountService = accountService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Account> getAccountByNumber(@PathVariable Long accountNumber) {
        Account account = _accountService.getAccountById(accountNumber);
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
    public ResponseEntity<String> createAccount() {
        return ResponseEntity.ok("Account created!");
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
