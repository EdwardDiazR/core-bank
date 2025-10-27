package com.example.nuevo_core.amortizationTable;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AmortizationTable {
    private long id;
    private long loanId;
    private List<AmortizationTableItem> item;


}

