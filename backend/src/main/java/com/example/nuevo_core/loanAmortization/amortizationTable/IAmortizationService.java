package com.example.nuevo_core.loanAmortization.amortizationTable;


import com.example.nuevo_core.loanAmortization.amortizationTableItem.AmortizationTableItem;

import java.math.BigDecimal;
import java.util.Optional;

public interface IAmortizationService {
    AmortizationTable generateAmortizationTable(Long loanId,
                                                BigDecimal installmentAmount,
                                                BigDecimal capital,
                                                BigDecimal interestRate,
                                                int term,
                                                int interestPeriodInMonths);

    AmortizationTable getAmortizationTableByLoanId(Long loanId);
    void saveAmortizationTable(AmortizationTable table);
    Optional<AmortizationTableItem> getAmortizationTableItemById(Long id);
}
