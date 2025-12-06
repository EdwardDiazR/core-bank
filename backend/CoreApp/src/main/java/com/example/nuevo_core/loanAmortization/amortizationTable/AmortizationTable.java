package com.example.nuevo_core.loanAmortization.amortizationTable;

import com.example.nuevo_core.loan.entity.Loan;
import com.example.nuevo_core.loanAmortization.amortizationTableItem.AmortizationTableItem;
import com.example.nuevo_core.loan.entity.LoanPayment;
import com.example.nuevo_core.utils.BooleanToNumberConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter @Setter
@Table(name = "amortization_table")
public class AmortizationTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loanId")
    @JsonIgnore
    private Loan loan; //todo: change to loan

    @OneToMany(mappedBy = "amortizationTable",
            cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.EAGER)
    private List<AmortizationTableItem> items;

    //@OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    @Nullable
    @JsonIgnore
    @Transient
    private List<LoanPayment> payments;

    @Convert(converter = BooleanToNumberConverter.class)
    @Column(name = "is_active")
    private boolean isActive;

}

