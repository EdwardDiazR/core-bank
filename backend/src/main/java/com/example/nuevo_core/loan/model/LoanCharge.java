package com.example.nuevo_core.loan.model;

import com.example.nuevo_core.loan.constants.LoanChargeType;
import com.example.nuevo_core.utils.BooleanToNumberConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loan_charge")
public class LoanCharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="payment_id")
    private Long paymentId;

    @Column(name="loan_id")
    private Long loanId;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="charge_date")
    private LocalDate chargeDate;

    @Column(name="is_paid")
    @Convert(converter = BooleanToNumberConverter.class)
    private boolean isPaid;

    @Column(name="type")
    @Enumerated(EnumType.STRING) //Mora, Comission etc.
    private LoanChargeType type;
}
