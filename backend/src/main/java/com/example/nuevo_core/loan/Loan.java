package com.example.nuevo_core.loan;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Loan {
    private long id;
    private String status;
    private String type;
    private double disbursementAmount;
    private float interestRate;
    private int term;
    private double capitalBalance;
    private double interestBalance;
    private double totalPaidInterest;
    private double projectedInterest;
    private double dailyInterestFactor;
    private double montoCuota;

    @Nullable
    private LocalDate nextPaymentDate;

    @Nullable
    private LocalDateTime disbursementDate;

    @Nullable
    private LocalDateTime lastPaymentDate;

    @Nullable
    private LocalDateTime lastInterestRateReviewDate;

    @Nonnull
    private LocalDateTime createdAt;
    private int oneCycleTimes;
    private int twoCycleTimes;
    private List<String> relateds;
    private  AmortizationTable amortizationTable;
}
