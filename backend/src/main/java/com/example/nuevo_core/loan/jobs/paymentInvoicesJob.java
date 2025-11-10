package com.example.nuevo_core.loan.jobs;

import com.example.nuevo_core.loan.interfaces.ILoanPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class paymentInvoicesJob {

    @Autowired
    private ILoanPaymentService _loanPaymentService;

    @Scheduled(cron = "0 0 0 * * *")
    public void generatePaymentInvoices() {

        _loanPaymentService.generateLoanPaymentInvoices();
    }
}
