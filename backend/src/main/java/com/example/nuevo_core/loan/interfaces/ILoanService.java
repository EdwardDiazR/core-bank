package com.example.nuevo_core.loan.interfaces;

import com.example.nuevo_core.constants.loans.LoanInterestPeriod;
import com.example.nuevo_core.loan.dto.loan.AdminLoanDto;
import com.example.nuevo_core.loan.dto.loan.CreateLoanDto;
import com.example.nuevo_core.loan.dto.loan.LoanDto;
import com.example.nuevo_core.loan.entity.Loan;

import java.math.BigDecimal;
import java.util.Optional;

public interface ILoanService {
    LoanDto getLoanByProductNumber(String number);

    AdminLoanDto getLoanDetailsToAdmin(Long loanId);

    Loan createLoan(CreateLoanDto loanDto);

    BigDecimal calculateCuota(BigDecimal capital,
                                     BigDecimal tasa,
                                     int meses
    );
    BigDecimal calculateDailyInterestFactor(BigDecimal amount,
                                            BigDecimal interestRate,
                                            int term
    );

    BigDecimal calculateProjectedInterest(BigDecimal amount,
                                          BigDecimal interestRate,
                                          int term,
                                          LoanInterestPeriod period);


    void deleteLoanById(Long loanId) throws Exception;


}
