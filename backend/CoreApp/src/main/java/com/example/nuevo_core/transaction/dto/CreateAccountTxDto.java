package com.example.nuevo_core.transaction.dto;

import com.example.nuevo_core.financialProduct.entity.FinancialProduct;
import com.example.nuevo_core.transaction.constants.TransactionCategory;
import com.example.nuevo_core.transaction.constants.TransactionStatus;
import com.example.nuevo_core.transaction.constants.TransactionType;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;

public record CreateAccountTxDto(@Nullable String description,
                                 BigDecimal amount,
                                 TransactionType type,
                                 String channel,
                                 TransactionStatus status,
                                 TransactionCategory category,
                                 FinancialProduct financialProductId,
                                 Long referenceId,
                                 String currency) {
}
