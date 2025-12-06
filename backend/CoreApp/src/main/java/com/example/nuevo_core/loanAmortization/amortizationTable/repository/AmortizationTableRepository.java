package com.example.nuevo_core.loanAmortization.amortizationTable.repository;

import com.example.nuevo_core.loan.entity.Loan;
import com.example.nuevo_core.loanAmortization.amortizationTable.AmortizationTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmortizationTableRepository extends JpaRepository<AmortizationTable, Long> {
    boolean existsByLoan(Loan loan);

    AmortizationTable findByLoan(Loan loan);
}
