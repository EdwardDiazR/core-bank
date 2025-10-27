package com.example.nuevo_core.amortizationTable;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class AmortizationTableItem {
    private UUID reference;
    private int paymentNumber;
    private BigDecimal cuota;
    private BigDecimal capital;
    private BigDecimal interes;
    private BigDecimal saldo;
    private LocalDate paymentDate;
    private boolean isPaid;
    private LocalDate paidDate;
}
