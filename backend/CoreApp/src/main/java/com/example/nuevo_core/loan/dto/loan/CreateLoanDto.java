package com.example.nuevo_core.loan.dto.loan;

import com.example.nuevo_core.constants.loans.LoanInterestPeriod;
import com.example.nuevo_core.financialProduct.constants.AccountSignType;
import com.example.nuevo_core.financialProduct.dto.CreateAccountRelativeDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

    public record CreateLoanDto(BigDecimal amount,
                                BigDecimal interestRate,
                                int termInMonths,
                                LoanInterestPeriod interestPeriodFrequency,
                                Set<CreateAccountRelativeDTO> relatives,
                                String type,
                                String currency,
                                AccountSignType signType,
                                LocalDate firstPaymentDate) {

}
