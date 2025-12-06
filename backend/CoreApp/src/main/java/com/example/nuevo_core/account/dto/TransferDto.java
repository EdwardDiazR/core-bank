package com.example.nuevo_core.account.dto;

import jakarta.annotation.Nullable;

import java.math.BigDecimal;

public record TransferDto(Long fromAccountId,
                          Long toAccountId,
                          BigDecimal amount,
                          @Nullable String desc) {
}
