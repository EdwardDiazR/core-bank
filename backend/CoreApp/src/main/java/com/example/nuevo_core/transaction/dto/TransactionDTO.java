package com.example.nuevo_core.transaction.dto;

import com.example.nuevo_core.transaction.constants.TransactionType;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.util.Optional;

public record TransactionDTO(BigDecimal amount,
                             BigDecimal afterBalance,
                             TransactionType type,
                             String description,
                             @Nullable Long referenceId ) {
}
