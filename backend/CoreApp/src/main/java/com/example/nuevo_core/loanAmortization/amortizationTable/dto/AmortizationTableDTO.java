package com.example.nuevo_core.loanAmortization.amortizationTable.dto;

import com.example.nuevo_core.loanAmortization.amortizationTableItem.AmortizationTableItem;

import java.util.List;

public record AmortizationTableDTO(String loanNumber, List<AmortizationTableItem> items) {
}
