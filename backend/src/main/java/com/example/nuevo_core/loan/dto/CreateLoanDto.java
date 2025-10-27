package com.example.nuevo_core.loan.dto;

import com.example.nuevo_core.constants.loans.LoanInterestPeriod;

import java.math.BigDecimal;
import java.util.List;

public record CreateLoanDto(BigDecimal amount,
                            BigDecimal interestRate,
                            int termInMonths,
                            LoanInterestPeriod interestPeriodFrequency,
                            List<String> relateds,
                            String type,
                            String currency) {

}
