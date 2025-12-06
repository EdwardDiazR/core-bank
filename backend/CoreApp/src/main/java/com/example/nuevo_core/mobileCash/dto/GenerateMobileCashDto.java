package com.example.nuevo_core.mobileCash.dto;

import java.math.BigDecimal;

public record GenerateMobileCashDto(String documentId,
                                    Long originAccount,
                                    BigDecimal amount) {
}
