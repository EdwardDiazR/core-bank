package com.example.nuevo_core.loan.jobs;

import com.example.nuevo_core.loan.interfaces.ILoanPaymentService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
public class loanAutopayJob {
    private final ILoanPaymentService _loanPaymentService;
    private final LocalDateTime now = LocalDateTime.now();

    public loanAutopayJob(ILoanPaymentService loanPaymentService) {
        _loanPaymentService = loanPaymentService;
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void executeAutoPayBatch() {
        String msj = String.format("Autopay attempted at %s", now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm a")));
        log.info(msj);

        List<Long> dueLoansIdToAutoPay = _loanPaymentService.getAllDueInstallmentsToAutopay();

        if(dueLoansIdToAutoPay.isEmpty()){
            log.info("No hay prestamos pendientes");
        }

        for (Long loanId : dueLoansIdToAutoPay) {
            try {
                _loanPaymentService.autoPayLoan(loanId);
                log.info("Autopay procesado para prestamo: {}", loanId);

            } catch (Exception e) {
                log.error("Error procesando prestamo: {}",loanId);
            }
        }

    }

}
