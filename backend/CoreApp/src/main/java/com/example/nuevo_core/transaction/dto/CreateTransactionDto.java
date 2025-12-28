package com.example.nuevo_core.transaction.dto;

import com.example.nuevo_core.financialProduct.entity.FinancialProduct;
import com.example.nuevo_core.transaction.constants.TransactionCategory;
import com.example.nuevo_core.transaction.constants.TransactionStatus;
import com.example.nuevo_core.transaction.constants.TransactionType;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;

public record CreateTransactionDto(@Nullable String description,
                                   BigDecimal amount,
                                   TransactionType type,
                                   String channel,
                                   TransactionStatus status,
                                   TransactionCategory category,
                                   FinancialProduct financialProduct,
                                   Long referenceId,
                                   BigDecimal balanceAfter,
                                   String currency) {
}
