package com.example.nuevo_core.loan.dto.loan;

import com.example.nuevo_core.loanAmortization.amortizationTable.AmortizationTable;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanDto(String number,
                      BigDecimal capitalBalance,
                      LocalDate nextPaymentDate,
                      BigDecimal installmentAmount
                      )  {
}
