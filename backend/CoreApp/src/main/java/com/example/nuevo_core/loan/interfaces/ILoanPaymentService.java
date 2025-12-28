package com.example.nuevo_core.loan.interfaces;

import com.example.nuevo_core.loan.entity.LoanCharge;
import com.example.nuevo_core.loan.entity.LoanPayment;
import com.example.nuevo_core.loan.dto.loanPayment.PayLoanDto;
import com.example.nuevo_core.loanAmortization.amortizationTableItem.AmortizationTableItem;

import java.time.LocalDate;
import java.util.List;

public interface ILoanPaymentService {
    List<Long> getAllDueInstallmentsToAutopay();
    List<LoanPayment> getDueInstallmentsByLoanId(Long loanId);
    void generateLoanPaymentInvoices();
    List<AmortizationTableItem> findPendingInstallmentsForInvoicing();
    List<LoanCharge> getDueChargesByLoanId(Long loanId);
    void autoPayLoan(Long loanId);
    void payLoan(PayLoanDto payLoanDto);
    List<LoanPayment> findOverDueInstallments(LocalDate date);

}
