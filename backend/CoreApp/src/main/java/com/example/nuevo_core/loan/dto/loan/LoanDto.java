package com.example.nuevo_core.loan.dto.loan;

import com.example.nuevo_core.financialProduct.dto.relative.RelativeDTO;
import com.example.nuevo_core.financialProduct.entity.FinancialProduct;
import com.example.nuevo_core.loanAmortization.amortizationTable.AmortizationTable;
import com.example.nuevo_core.loanAmortization.amortizationTable.dto.AmortizationTableDTO;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record LoanDto(String number,
                      BigDecimal capitalBalance,
                      BigDecimal interestBalance,
                      BigDecimal interestRate,
                      BigDecimal installmentAmount,
                      LocalDate nextPaymentDate,
                      LocalDate dueDate,
                      int paymentsMade,
                      int paymentsPending,
                      @Nullable FinancialProduct financialProduct,
                      @Nullable AmortizationTableDTO amortizationTable
){};

