package com.example.nuevo_core.financialProduct.entity;

import com.example.nuevo_core.financialProduct.constants.AccountRelatveCondition;
import com.example.nuevo_core.utils.BooleanToNumberConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "account_relative")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancialProductRelative {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private FinancialProduct financialProduct;
   // @OneToOne(mappedBy = "id",fetch = FetchType.LAZY)
    private Long customerId;
    @Enumerated(EnumType.STRING)
    private AccountRelatveCondition relationCondition;

    @Column(name = "is_principal")
    @Convert(converter = BooleanToNumberConverter.class)
    private boolean isPrincipal;
}
