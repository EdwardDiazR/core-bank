package com.example.nuevo_core.loan.dto.loanPayment;

import com.example.nuevo_core.loan.entity.Loan;

import java.math.BigDecimal;


public record PayLoanDto(
        Loan loan,
        BigDecimal amount,
        String source) {
}
