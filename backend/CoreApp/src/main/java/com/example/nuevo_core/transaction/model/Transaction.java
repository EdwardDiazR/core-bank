package com.example.nuevo_core.transaction.model;

import com.example.nuevo_core.financialProduct.entity.FinancialProduct;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "financial_product_id")
    private FinancialProduct financialProduct;

    @Column(name = "description")
    private String description;

    @Column(name = "effective_date")
    private LocalDateTime effectiveDate;

    @Column(name = "entry_date")
    private LocalDateTime entryDate;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "type")
    private String type;//Debit or credit

    @Column(name = "channel")
    private String channel;
}
