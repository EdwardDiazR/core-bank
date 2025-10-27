package com.example.nuevo_core.loan;

import com.example.nuevo_core.amortizationTable.AmortizationTable;
import com.example.nuevo_core.constants.loans.LoanInterestPeriod;
import com.example.nuevo_core.loan.dto.CreateLoanDto;

import java.math.BigDecimal;

public interface ILoanService {
    Loan getLoanById(Long id);

    Loan createLoan(CreateLoanDto loanDto);

    BigDecimal calculateProjectedInterest(BigDecimal amount,
                                          BigDecimal interestRate,
                                          int term,
                                          LoanInterestPeriod period);

    BigDecimal calculateDailyInterestFactor(BigDecimal amount,
                                            BigDecimal interestRate,
                                            int term
    )
            ;

    AmortizationTable generateAmortizationTable(Long loanId,
                                                BigDecimal amount,
                                                BigDecimal interest,
                                                int term,
                                                int interestPeriodInMonths);

    void deleteLoanById(Long loanId);
}
