package com.example.nuevo_core.financialProduct.entity;

import com.example.nuevo_core.financialProduct.constants.AccountRelatveCondition;
import com.example.nuevo_core.utils.BooleanToNumberConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "financial_product_relative")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancialProductRelative {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private FinancialProduct financialProduct;

    // @OneToOne(mappedBy = "id",fetch = FetchType.LAZY)
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    private AccountRelatveCondition relationCondition;

    @Column(name = "is_principal")
    @Convert(converter = BooleanToNumberConverter.class)
    private boolean isPrincipal;

}
