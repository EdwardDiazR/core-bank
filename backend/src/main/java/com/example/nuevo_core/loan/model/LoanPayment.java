package com.example.nuevo_core.loan.model;

import com.example.nuevo_core.loanAmortization.amortizationTableItem.AmortizationTableItem;
import com.example.nuevo_core.loan.constants.PaymentStatus;
import com.example.nuevo_core.utils.BooleanToNumberConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loan_payment")
public class LoanPayment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "loan_id", nullable = false)
    @JsonIgnore
    private Long loanId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amortization_table_item_id", nullable = false, unique = true)
    @JsonIgnore
    private AmortizationTableItem amortizationItemId;

    @Column(name = "installment_amount")
    private BigDecimal installmentAmount;

    @Column(name = "outstanding_principal_due")
    private BigDecimal outstandingPrincipalDue;

    @Column(name = "interest_due")
    private BigDecimal interestDue;

    @Column(name = "outstanding_principal_paid")
    private BigDecimal outstandingPrincipalPaid;

    @Column(name = "interest_paid")
    private BigDecimal interestPaid;

    @Column(name = "pending_installment_balance")
    private BigDecimal pendingInstallmentBalance;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "last_payment_date")
    @Nullable
    private LocalDateTime lastPaymentDate;

    @Convert(converter = BooleanToNumberConverter.class)
    @Column(name = "is_paid")
    private boolean isPaid;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @PrePersist
    @PreUpdate
    //todo: validate in every update that total equals to outstanding+interest balance
    public void recalculateTotal() {
        BigDecimal totalDue = this.getOutstandingPrincipalDue()
                .add(this.getInterestDue());

        BigDecimal totalPaid = this.getInterestPaid().add(this.getOutstandingPrincipalPaid());
        this.pendingInstallmentBalance = totalDue.subtract(totalPaid);
    }
}
