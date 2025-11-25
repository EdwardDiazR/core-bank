package com.example.nuevo_core.loan.dto.loan;

import com.example.nuevo_core.constants.loans.LoanInterestPeriod;
import com.example.nuevo_core.financialProduct.constants.AccountSignType;
import com.example.nuevo_core.financialProduct.dto.CreateAccountRelativeDto;

import java.math.BigDecimal;
import java.util.Set;

    public record CreateLoanDto(BigDecimal amount,
                                BigDecimal interestRate,
                                int termInMonths,
                                LoanInterestPeriod interestPeriodFrequency,
                                Set<CreateAccountRelativeDto> relateds,
                                String type,
                                String currency,
                                AccountSignType signType) {

}
