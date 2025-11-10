package com.example.nuevo_core.loanAmortization.amortizationTableItem;

import com.example.nuevo_core.loanAmortization.amortizationTable.AmortizationTable;
import com.example.nuevo_core.utils.BooleanToNumberConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "amortization_table_item")
public class AmortizationTableItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amort_item_seq")
    @SequenceGenerator(name = "amort_item_seq", sequenceName = "amort_item_seq", allocationSize = 50)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AMORTIZATION_TABLE_ID", nullable = false)
    @JsonIgnore
    private AmortizationTable amortizationTable;

    @Column(name = "installment_number")
    private int installmentNumber;

    @Column(name = "cuota")
    private BigDecimal cuota;

    @Column(name = "capital")
    private BigDecimal capital;

    @Column(name = "interes")
    private BigDecimal interes;

    @Column(name = "charges")
    private BigDecimal charges;

    @Column(name = "saldo")
    private BigDecimal saldo;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "is_paid")
    @Convert(converter = BooleanToNumberConverter.class)
    private boolean isPaid;

    @Column(name = "paid_date")
    private LocalDate paidDate;
}
