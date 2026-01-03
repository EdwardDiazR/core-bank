package com.example.nuevo_core.loan.entity;

import com.example.nuevo_core.financialProduct.entity.FinancialProduct;
import com.example.nuevo_core.loanAmortization.amortizationTable.AmortizationTable;
import com.example.nuevo_core.constants.loans.PaymentFrequency;
import com.example.nuevo_core.loan.constants.LoanStatus;
import com.example.nuevo_core.utils.BooleanToNumberConverter;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "loan")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne()
    @JoinColumn(name = "financial_product_id")
    private FinancialProduct financialProduct;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Column(name = "type")
    private String type;

    @Column(name = "currency")
    private String currency;

    @Column(name = "principal_amount")
    @DecimalMin(value = "1.00", message = "El préstamo debe ser mayor a 1")
    private BigDecimal principalAmount; //Monto del desembolso

    @Column(name = "available_amount_for_disbursement")
    private BigDecimal availableAmountForDisbursement;

    @Column(name = "outstanding_principal_balance")
    private BigDecimal outstandingPrincipalAmount; //Monto de capital

    @Column(name = "interest_balance")
    private BigDecimal interestBalance;


    @Column(name = "interest_rate", precision = 12, scale = 10)
    private BigDecimal interestRate;

    @Column(name = "term_in_months")
    @Min(1)
    @Max(240)
    private int termInMonths;

    @Column(name = "payment_frequency")
    @Enumerated(EnumType.STRING)
    private PaymentFrequency paymentFrequency; //Monthly, Weekly,Daily

    @Column(name = "daily_interest_factor")
    private BigDecimal dailyInterestFactor;

    @Column(name = "installment_amount")
    private BigDecimal installmentAmount; //Cuota

    @Column(name = "late_fee_rate", precision = 12, scale = 10)

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

//    @Column(name = "next_interest_rate_review_date")
//    @Nullable
//    private LocalDateTime nextInterestRateReviewDate;

    @Column(name = "due_date")
    @Nullable
    private LocalDate dueDate;

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
    @OneToOne(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private AmortizationTable amortizationTable;

    @OneToMany(mappedBy = "loan", fetch = FetchType.LAZY)
    private List<LoanPayment> payments;
}
