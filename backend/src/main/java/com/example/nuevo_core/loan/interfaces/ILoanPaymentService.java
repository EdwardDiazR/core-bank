package com.example.nuevo_core.loan.interfaces;

import com.example.nuevo_core.loan.model.LoanPayment;
import com.example.nuevo_core.loan.dto.loanPayment.PayLoanDto;

import java.time.LocalDate;
import java.util.List;

public interface ILoanPaymentService {
    List<Long> getAllDueInstallmentsToAutopay();
    List<LoanPayment> getDueInstallmentsByLoanId(Long loanId);
    void generateLoanPaymentInvoices();

    void autoPayLoan(Long loanId);
    void payLoan(PayLoanDto payLoanDto);
    List<LoanPayment> findOverDueInstallments(LocalDate date);

}
