package com.example.nuevo_core.mobileCash.dto;

import com.example.nuevo_core.mobileCash.constants.MobileCashStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MobileCashDto(String otp,
                            BigDecimal amount,
                            Long originAccount,
                            String customerDocumentId,
                            LocalDateTime expireAt,
                            String status) {
}
