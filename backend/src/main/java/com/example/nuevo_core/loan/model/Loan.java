package com.example.nuevo_core.loan.model;

import com.example.nuevo_core.loanAmortization.amortizationTable.AmortizationTable;
import com.example.nuevo_core.constants.loans.PaymentFrequency;
import com.example.nuevo_core.loan.constants.LoanStatus;
import com.example.nuevo_core.utils.BooleanToNumberConverter;
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
@SequenceGenerator(
        name = "global_seq",
        sequenceName = "global_seq",
        allocationSize = 1
)
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @Column(name = "id")
    private Long id = null;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Column(name = "type")
    private String type;

    @Column(name = "currency")
    private String currency;

    @Column(name = "principal_amount")
    private BigDecimal principalAmount; //Monto del desembolso

    @Column(name = "available_amount_for_disbursement")
    private BigDecimal availableAmountForDisbursement;

    @Column(name = "outstanding_principal_balance")
    private BigDecimal outstandingPrincipalAmount; //Monto de capital

    @Column(name = "interest_balance")
    private BigDecimal interestBalance;

    @Column(name = "interest_rate", precision = 7,scale = 5)
    private BigDecimal interestRate;

    @Column(name = "term_in_months")
    private int termInMonths;

    @Column(name = "payment_frequency")
    @Enumerated(EnumType.STRING)
    private PaymentFrequency paymentFrequency; //Monthly, Weekly,Daily

    @Column(name = "daily_interest_factor")
    private BigDecimal dailyInterestFactor;

    @Column(name = "installment_amount")
    private BigDecimal installmentAmount; //Cuota

    @Column(name = "late_fee_rate")
    private BigDecimal lateFeeRate;

    @Column(name = "late_fee_balance")
    private BigDecimal lateFeeBalance = BigDecimal.ZERO;

    @Column(name = "total_installment_balance")
    BigDecimal totalInstallmentBalance; // Installment + lateFeeBalance

    @Column(name = "total_paid_interest")
    private BigDecimal totalPaidInterest;

    @Column(name = "projected_interest")
    private BigDecimal projectedInterest;

    @Column(name = "one_cycle_times")
    private int oneCycleTimes;

    @Column(name = "two_cycle_times")
    private int twoCycleTimes;

    @Column(name = "payments_made")
    private Integer paymentsMade;

    @Column(name = "payments_pending")
    private Integer paymentsPending;

    ///
    @Column(name = "first_payment_date")
    @Nullable
    private LocalDate firstPaymentDate;

    @Column(name = "next_payment_date")
    @Nullable
    private LocalDate nextPaymentDate;

    @Column(name = "last_payment_date")
    @Nullable
    private LocalDateTime lastPaymentDate = null;

    @Column(name = "interest_balance_update_date")
    private LocalDateTime lastInterestBalanceUpdateDate = null;

    @Column(name = "disbursement_amount_date")
    @Nullable
    private LocalDateTime disbursementDate;

    @Column(name = "last_interest_rate_review_date")
    @Nullable
    private LocalDateTime lastInterestRateReviewDate;

    @Column(name = "due_date")
    @Nullable
    private LocalDate dueDate;

    @Column(name = "created_at")
    @Nonnull
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Nullable
    private LocalDateTime updatedAt;
    ///

    ///

    @Column(name = "linked_account")
    @Nullable
    private Long linkedAccount;

    @Convert(converter = BooleanToNumberConverter.class)
    @Column(name = "can_auto_debit")
    private Boolean canAutoDebit;

    @Convert(converter = BooleanToNumberConverter.class)
    @Column(name = "is_line_of_credit")
    private Boolean isLineOfCredit;

    @Convert(converter = BooleanToNumberConverter.class)
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Nullable
    @Transient
    private List<String> relateds;

    @Nullable
    @Transient
    private AmortizationTable amortizationTable = null;


}
