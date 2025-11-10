package com.example.nuevo_core.loan.repository;

import com.example.nuevo_core.loan.model.Loan;
import com.example.nuevo_core.loanAmortization.amortizationTableItem.AmortizationTableItem;
import com.example.nuevo_core.loan.model.LoanPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanPaymentRepository extends JpaRepository<LoanPayment, Long> {
    @Query("""
                SELECT DISTINCT p.loanId
                FROM LoanPayment p
                WHERE p.status IN ('PENDING', 'PARTIAL')
                  AND p.dueDate <= :date
            """)
    List<Long> findLoansWithDueInstallmentsToAutopay(@Param("date") LocalDate date);

    @Query("""
                SELECT i
                FROM AmortizationTableItem i
                WHERE i.isPaid = false
                AND i.paymentDate <= :today
            """)

    List<AmortizationTableItem> generateLoanPayments(
            @Param("today") LocalDate today,
            @Param("fiveDaysLater") LocalDate fiveDaysLater);

    Boolean existsByAmortizationItemId(AmortizationTableItem amortizationItemId);

    @Query("""
                SELECT p
                FROM LoanPayment p
                WHERE p.loanId = :loanId
                AND p.isPaid = false
                AND p.pendingInstallmentBalance > 0
            """)
    List<LoanPayment> findPendingInstallmentsByLoanId(@Param("loanId") Long loanId);

    @Query("""
                SELECT  p
                FROM LoanPayment p
                WHERE p.dueDate < :date AND p.status != 'PAID'
            """)
    List<LoanPayment> findOverDueInstallments(@Param("date") LocalDate dateWithGraceDays);
}
