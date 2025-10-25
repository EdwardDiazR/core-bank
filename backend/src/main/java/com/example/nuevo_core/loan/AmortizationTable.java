package com.example.nuevo_core.loan;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AmortizationTable {
    private long id;
    private long loanId;
    private List<AmortizationTableItem> item;


}

@Data
@Builder
class AmortizationTableItem {
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
