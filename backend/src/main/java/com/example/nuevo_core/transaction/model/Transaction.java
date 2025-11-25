package com.example.nuevo_core.transaction.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private Long id;
    private Long accountId;
    private String description;
    private LocalDateTime date;
    private BigDecimal amount;
    private int currencyCode;

    private String type; //Debit or credit
    private String channel;
}
