package com.example.nuevo_core.financialProduct.entity;

import com.example.nuevo_core.financialProduct.constants.AccountSignType;
import com.example.nuevo_core.financialProduct.constants.ProductType;
import com.example.nuevo_core.transaction.model.Transaction;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//seq_financial_product ,seq_account_number ,seq_loan_number ,seq_cd_number
@Entity
@Table(name = "financial_product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinancialProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_number", unique = true, nullable = false, length = 20)
    private String productNumber;

    @Column(name = "public_id", unique = true,nullable = false)
    private String publicId;

    @Column(name = "regional_product_number", unique = true, nullable = false,length = 35)
    private String regionalProductNumber = "";

    @Column(name = "principal_customer_id", nullable = false)
    private Long principalCustomerId;

    @Column(name = "product_type")
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Enumerated(EnumType.STRING)
    private AccountSignType signType; //unique, indistinct, joint

    @OneToMany(mappedBy = "financialProduct",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private Set<FinancialProductRelative> relatives = new HashSet<>();

    @OneToMany(mappedBy = "financialProduct",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @Nullable
    private List<Transaction> transactions = new ArrayList<>();
  /*  public void addRelatives(Set<CreateAccountRelativeDto> list) {
        for (CreateAccountRelativeDto relative : list) {
            FinancialProductRelative rl = FinancialProductRelative.builder()
                    .customerId(relative.customerId())
                    .relationCondition(relative.accountRelatveCondition())
                    .isPrincipal(relative.isPrincipal())
                    .financialProduct(this)
                    .build();
            relatives.add(rl);
        }
    }*/

    public void removeRelative(FinancialProductRelative relative) {
        relatives.remove(relative);
    }

}
