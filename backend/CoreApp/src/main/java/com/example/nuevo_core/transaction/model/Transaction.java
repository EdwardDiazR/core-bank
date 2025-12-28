package com.example.nuevo_core.transaction.model;

import com.example.nuevo_core.financialProduct.entity.FinancialProduct;
import com.example.nuevo_core.transaction.constants.TransactionCategory;
import com.example.nuevo_core.transaction.constants.TransactionStatus;
import com.example.nuevo_core.transaction.constants.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tx_financial_product_id")
    @JsonIgnore
    private FinancialProduct financialProduct;

    @Column(name = "tx_description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "tx_category")
    private TransactionCategory category;

    @Column(name = "tx_posted_date")
    private LocalDateTime effectiveDate;

    @Column(name = "tx_entry_date")
    private LocalDateTime entryDate;

    @Column(name = "tx_amount")
    private BigDecimal amount;

    @Column(name = "tx_currency")
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "tx_type")
    private TransactionType type;//Debit or credit

    @Column(name = "tx_channel")
    private String channel;

    @Column(name = "tx_balance_after")
    private BigDecimal balanceAfter;

    @Enumerated(EnumType.STRING)
    @Column(name = "tx_status")
    private TransactionStatus status;

    @Column(name = "tx_reference_id") //todo: UNIQUE BETWEEN (CREDIT OR DEBIT, FINANCIAL PRODUCT, REFERENCE ID)
    private Long referenceId;
}
