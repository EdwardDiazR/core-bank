package com.example.nuevo_core.loan.interfaces;

import com.example.nuevo_core.loan.entity.Loan;

public interface ILoanInterestService {
    void processLoanInterestAccrualBatch();
    void addAccruedInterestToInterestBalance(Loan loan);
}
