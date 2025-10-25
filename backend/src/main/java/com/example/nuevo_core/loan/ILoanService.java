package com.example.nuevo_core.loan;

import com.example.nuevo_core.constants.loans.LoanInterestPeriod;
import com.example.nuevo_core.loan.dto.CreateLoanDto;

import java.math.BigDecimal;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

public interface ILoanService {
    Loan getLoanById(long id);
    Loan createLoan(CreateLoanDto loanDto);
    BigDecimal calculateProjectedInterest(double amount, float interestRate, int term, LoanInterestPeriod period);
    double calculateDailyInterestFactor(double amount,
                                  float interestRate,
                                  int term,
                                  LoanInterestPeriod period);

    AmortizationTable generateAmortizationTable(double amount, float interest, int term);
}
