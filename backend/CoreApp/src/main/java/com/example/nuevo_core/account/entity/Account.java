package com.example.nuevo_core.account.entity;

import com.example.nuevo_core.account.constants.AccountCurrency;
import com.example.nuevo_core.account.constants.AccountStatus;
import com.example.nuevo_core.financialProduct.entity.FinancialProduct;
import com.example.nuevo_core.financialProduct.entity.FinancialProductRelative;
import com.example.nuevo_core.transaction.model.Transaction;
import com.example.nuevo_core.utils.BooleanToNumberConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "account")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "financial_product_id")
    private FinancialProduct financialProduct;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private AccountCurrency currency;

    @Column(name = "total_balance")
    private BigDecimal totalBalance;

    @Column(name = "in_transit_balance")
    private BigDecimal inTransitAmount;

    @Column(name = "in_hold_amount")
    private BigDecimal holdAmount;

    @Column(name = "available_balance")
    private BigDecimal availableBalance;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_deposit_date")
    private LocalDateTime lastDepositDate;

    @Column(name = "last_activity_date")
    private LocalDateTime lastActivityDate;

    @Column(name = "has_first_deposit")
    @Convert(converter = BooleanToNumberConverter.class)
    private boolean hasFirstDeposit;

    @Column(name = "allow_debits")
    @Convert(converter = BooleanToNumberConverter.class)
    private boolean allowDebits;

    @Column(name = "allow_credits")
    @Convert(converter = BooleanToNumberConverter.class)
    private boolean allowCredits;
}
