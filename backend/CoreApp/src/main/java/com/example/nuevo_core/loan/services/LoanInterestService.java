package com.example.nuevo_core.loan.services;

import com.example.nuevo_core.loan.interfaces.ILoanInterestService;
import com.example.nuevo_core.loan.interfaces.ILoanService;
import com.example.nuevo_core.loan.entity.Loan;
import com.example.nuevo_core.loan.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LoanInterestService implements ILoanInterestService {

    private static final BigDecimal DAYS_IN_YEAR = new BigDecimal(360);

    private final ILoanService _loanService;
    private final LoanRepository loanRepository;

    private LocalDateTime today = LocalDateTime.now();

    public LoanInterestService(ILoanService loanService, LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
        _loanService = loanService;
    }

    public void processLoanInterestAccrualBatch() {

        List<Loan> activeLoans = loanRepository.getLoansByStatusNormal();

        for (Loan loan : activeLoans) {
            addAccruedInterestToInterestBalance(loan);
        }

        loanRepository.saveAll(activeLoans);
    }

    public void addAccruedInterestToInterestBalance(Loan loan) {
        long daysToAccrue;
        LocalDateTime lastInterestAccruedDate = loan.getLastInterestBalanceUpdateDate();


        if (lastInterestAccruedDate == null) {
            daysToAccrue = 1;
        } else {
            daysToAccrue = ChronoUnit.DAYS.between(lastInterestAccruedDate, today.toLocalDate());
        }

        if (daysToAccrue > 0) {

            BigDecimal outstandingBalance = loan.getOutstandingPrincipalAmount();
            BigDecimal interestRate = loan.getInterestRate();

            BigDecimal dailyInterest = outstandingBalance
                    .multiply(interestRate)
                    .divide(DAYS_IN_YEAR, RoundingMode.HALF_UP);

            BigDecimal interestBalance = loan.getInterestBalance();
            BigDecimal dailyInterestFactor = loan.getDailyInterestFactor();

            //Total to add, based on days from last accrual
            BigDecimal totalToAdd = dailyInterest.multiply(new BigDecimal(daysToAccrue));

            loan.setInterestBalance(interestBalance.add(totalToAdd));
            loan.setLastInterestBalanceUpdateDate(today);

            loanRepository.save(loan);
        }
    }
}
