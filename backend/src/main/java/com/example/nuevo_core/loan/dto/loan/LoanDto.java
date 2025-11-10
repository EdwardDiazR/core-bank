package com.example.nuevo_core.loan.dto.loan;

import java.time.LocalDate;

public record LoanDto(long id,
                      double capitalBalance,
                      LocalDate nextPaymentDate,
                      double installmentAmount
                      )  {
}
