package com.example.nuevo_core.loan.services;

import com.example.nuevo_core.loan.interfaces.ILoanInterestService;
import com.example.nuevo_core.loan.interfaces.ILoanService;
import com.example.nuevo_core.loan.model.Loan;
import com.example.nuevo_core.loan.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LoanInterestService implements ILoanInterestService {

    private final ILoanService _loanService;
    @Autowired
    private LoanRepository loanRepository;

    private LocalDateTime today = LocalDateTime.now();

    public LoanInterestService(ILoanService loanService) {
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
        LocalDateTime lastInterestAccruedDate = loan.getLastInterestBalanceUpdateDate();

        long daysToAccrue = ChronoUnit.DAYS.between(lastInterestAccruedDate, today.toLocalDate());

        if (daysToAccrue > 0) {
            BigDecimal interestBalance = loan.getInterestBalance();
            BigDecimal dailyInterestFactor = loan.getDailyInterestFactor();

            //Total to add, based on days from last accrual
            BigDecimal totalToAdd = dailyInterestFactor.multiply(new BigDecimal(daysToAccrue));

            loan.setInterestBalance(interestBalance.add(totalToAdd));
            loan.setLastInterestBalanceUpdateDate(today);
            // loanRepository.save(loan);
        }
    }

}
