package com.example.nuevo_core.loanAmortization.amortizationTable;

import com.example.nuevo_core.loanAmortization.amortizationTableItem.AmortizationTableItem;
import com.example.nuevo_core.loan.model.LoanPayment;
import com.example.nuevo_core.utils.BooleanToNumberConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "amortization_table")
public class AmortizationTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "loan_id")
    private Long loanId;

    @OneToMany(mappedBy = "amortizationTable", cascade = CascadeType.ALL, orphanRemoval = true)
    @Nullable
    private List<AmortizationTableItem> item;

    //@OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    @Nullable
    @JsonIgnore
    @Transient
    private List<LoanPayment> payments;

    @Convert(converter = BooleanToNumberConverter.class)
    @Column(name="is_active")
    private boolean isActive;

}

