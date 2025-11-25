package com.example.nuevo_core.mobileCash.dto;

import java.math.BigDecimal;

public record RedeemMobileCashDto(String documentId,
                                  String otp,
                                  BigDecimal amount) {
}
