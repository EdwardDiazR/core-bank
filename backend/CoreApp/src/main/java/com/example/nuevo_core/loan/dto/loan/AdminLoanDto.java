package com.example.nuevo_core.loan.dto.loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoanDto {
    private long id;
    private BigDecimal montoCuota;
    private LocalDate nextPaymentDate;
}
