package com.example.nuevo_core.financialProduct.entity;

import com.example.nuevo_core.financialProduct.constants.AccountSignType;
import com.example.nuevo_core.financialProduct.constants.ProductType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Set;

//seq_financial_product ,seq_account_number ,seq_loan_number ,seq_cd_number
@SuperBuilder
@Entity
@Table(name = "financial_product")
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(of = {"productNumber"})
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FinancialProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_number", unique = true, nullable = false, length = 20)
    private String productNumber;

    @Column(name = "principal_customer_id", nullable = false)
    private Long principalCustomerId;

    @Column(name = "product_type")
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    @Enumerated(EnumType.STRING)
    private AccountSignType signType; //unique, indistinct, joint

    @OneToMany(mappedBy = "financialProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FinancialProductRelative> relatives;

    public void addRelative(FinancialProductRelative relative) {
        relatives.add(relative);
        relative.setFinancialProduct(this);
    }

    public void removeRelative(FinancialProductRelative relative) {
        relatives.remove(relative);
    }

}
