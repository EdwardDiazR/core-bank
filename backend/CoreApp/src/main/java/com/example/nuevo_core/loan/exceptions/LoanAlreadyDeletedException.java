package com.example.nuevo_core.loan.exceptions;

public class LoanAlreadyDeletedException extends RuntimeException {
    public LoanAlreadyDeletedException(String message) {
        super(message);
    }
}
