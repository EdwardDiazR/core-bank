package com.example.nuevo_core.loan;

import com.example.nuevo_core.amortizationTable.AmortizationTable;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "loan")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id = null;

    @Column(name = "status")
    private String status;

    @Column(name = "type")
    private String type;

    @Column(name="currency")
    private String currency;

    @Column(name = "disbursement_amount")
    private BigDecimal disbursementAmount;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column(name = "term")
    private int term;

    @Column(name = "capital_balance")
    private BigDecimal capitalBalance;

    @Column(name = "interest_balance")
    private BigDecimal interestBalance;

    @Column(name = "total_paid_interest")
    private BigDecimal totalPaidInterest;

    @Column(name = "projected_interest")
    private BigDecimal projectedInterest;

    @Column(name = "daily_interest_factor")
    private BigDecimal dailyInterestFactor;

    @Column(name = "monto_cuota")
    private BigDecimal montoCuota;

    @Column(name = "next_payment_date")
    @Nullable
    private LocalDate nextPaymentDate;

    @Column(name = "disbursement_amount_date")
    @Nullable
    private LocalDateTime disbursementDate;

    @Column(name = "last_payment_date")
    @Nullable
    private LocalDateTime lastPaymentDate;

    @Column(name = "last_interest_rate_review_date")
    @Nullable
    private LocalDateTime lastInterestRateReviewDate;

    @Column(name = "created_at")
    @Nonnull
    private LocalDateTime createdAt;

    @Column(name = "one_cycle_times")
    private int oneCycleTimes;

    @Column(name = "two_cycle_times")
    private int twoCycleTimes;

    @Nullable
    @Transient
    private List<String> relateds;

    @Nullable
    @Transient
    private AmortizationTable amortizationTable = null;

    @Column(name = "is_line_of_credit")
    private Boolean isLineOfCredit;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "amount_available_for_disbursement")
    private BigDecimal availableForDisbursement;
}
