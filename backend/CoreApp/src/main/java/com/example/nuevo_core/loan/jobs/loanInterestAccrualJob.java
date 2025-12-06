package com.example.nuevo_core.loan.jobs;

import com.example.nuevo_core.loan.interfaces.ILoanInterestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class loanInterestAccrualJob {

    private final ILoanInterestService _loanInterestService;

    public loanInterestAccrualJob(ILoanInterestService loanInterestService) {
        _loanInterestService = loanInterestService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void addDailyInterestFactor() {
        try {
            //TODO: days to accrue  -
            // Esto es para aplicar los intereses del proximo dia laborable:
            // Ej: si hoy es sabado aplicar, los del sabado domingo y lunes

            log.info("Started add accrued interest to interest balance");
            _loanInterestService.processLoanInterestAccrualBatch();
        } catch (Exception e) {
            log.error("Error at adding interest balance");
        }
    }

}
