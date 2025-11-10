package com.example.nuevo_core.loan.services;

import com.example.nuevo_core.loan.interfaces.ILoanPaymentService;
import com.example.nuevo_core.loan.model.Loan;
import com.example.nuevo_core.loan.model.LoanCharge;
import com.example.nuevo_core.loan.model.LoanPayment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanChargeService {
    private final int GRACE_DAYS = 3;
    private final LocalDate today = LocalDate.now();
    private final ILoanPaymentService _loanPaymentService;

    public LoanChargeService(ILoanPaymentService loanPaymentService) {
        _loanPaymentService = loanPaymentService;
    }

    public List<LoanCharge> getDueChargesByLoanId(Long loanId) {

        return new ArrayList<>();
    }

    public void generateChargesByDueInstallments() {
        LocalDate dateWithGraceDays = LocalDate.now().plusDays(GRACE_DAYS);
        List<LoanPayment> overDueInstallments = _loanPaymentService.findOverDueInstallments(dateWithGraceDays);

        for (LoanPayment overDueInstallment : overDueInstallments) {
            LocalDate graceEndDate = overDueInstallment.getDueDate().plusDays(GRACE_DAYS);
            var daysOverdue = ChronoUnit.DAYS.between(graceEndDate, today);

            System.out.println(overDueInstallment.getDueDate());
        }


    }

    public BigDecimal calculateMora(Long loanId) {
        return BigDecimal.ONE;
    }

}
