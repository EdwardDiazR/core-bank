package com.example.nuevo_core.loan;


import com.example.nuevo_core.constants.loans.LoanInterestPeriod;
import com.example.nuevo_core.loan.dto.CreateLoanDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/loan")
public class LoanController {
    private final ILoanService _loanService;

    public LoanController(ILoanService loanService) {
        _loanService = loanService;
    }

    @PostMapping()
    public ResponseEntity<Loan> createLoan(@RequestBody CreateLoanDto loanDto) {
        Loan loan = _loanService.createLoan(new CreateLoanDto(250000,
                10f,
                12,
                LoanInterestPeriod.ANNUAL));
        return ResponseEntity.ok(loan);
    }

    @GetMapping()
    public ResponseEntity<Loan> getLoanById(@RequestParam long id) {
        Loan loan = _loanService.getLoanById(id);
        return ResponseEntity.ok(loan);
    }

}
