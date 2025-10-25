package com.example.nuevo_core.loan.dto;

import com.example.nuevo_core.constants.loans.LoanInterestPeriod;

public record CreateLoanDto(double amount,
                            float interestRate,
                            int termInMonths,
                            LoanInterestPeriod interestPeriodFrequency){}
