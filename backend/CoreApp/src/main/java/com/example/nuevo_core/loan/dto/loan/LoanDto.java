package com.example.nuevo_core.loan.dto.loan;

import com.example.nuevo_core.financialProduct.dto.relative.RelativeDTO;
import com.example.nuevo_core.loan.constants.LoanStatus;
import com.example.nuevo_core.loan.entity.LoanPayment;
import com.example.nuevo_core.loanAmortization.amortizationTable.dto.AmortizationTableDTO;
import com.example.nuevo_core.transaction.dto.TransactionDTO;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class LoanDTO {

    /*General information*/
    private String number;
    private String currency;

    private LoanStatus status;
    private int termInMonths;
    private int paymentsMade;
    private int paymentsPending;

    /*Balances information*/
    private BigDecimal principalAmount;
    private BigDecimal outstandingPrincipalBalance;
    private BigDecimal availableAmountForDisbursement;
    private BigDecimal interestBalance;
    private BigDecimal interestRate;
    private BigDecimal installmentAmount;

    /*Payments information*/
    List<TransactionDTO> transactions;
    List<LoanPayment> pendingInstallments;

    @Nullable
    AmortizationTableDTO amortizationTable;

    /*Dates*/
    private LocalDate nextPaymentDate;
    private LocalDateTime lastPaymentDate;
    private LocalDate dueDate;
    LocalDateTime lastInterestRateReviewDate;



    /*Customer information*/
    Set<RelativeDTO> relatives;


}
