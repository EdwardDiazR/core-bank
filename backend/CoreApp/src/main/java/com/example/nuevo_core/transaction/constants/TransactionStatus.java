package com.example.nuevo_core.transaction.constants;

public enum TransactionStatus {
    PENDING,
    IN_PROCESS,
    COMPLETED,
    FAILED,
    CANCELLED,
    REVERSED,
    ADJUSTED,

    AUTHORIZED,
    CAPTURED,
    SETTLED,
    RETURNED,
    HOLD,
    RELEASED,

    REVIEW,
    BLOCKED,
    EXPIRED
}
