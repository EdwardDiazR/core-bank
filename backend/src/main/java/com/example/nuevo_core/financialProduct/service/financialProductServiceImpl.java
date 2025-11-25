package com.example.nuevo_core.financialProduct.service;

import com.example.nuevo_core.financialProduct.constants.ProductType;
import com.example.nuevo_core.financialProduct.dto.CreateAccountRelativeDto;
import com.example.nuevo_core.financialProduct.dto.CreateFinancialProductDto;
import com.example.nuevo_core.financialProduct.entity.FinancialProductRelative;
import com.example.nuevo_core.financialProduct.entity.FinancialProduct;
import com.example.nuevo_core.financialProduct.interfaces.FinancialProductService;
import com.example.nuevo_core.financialProduct.repository.FinancialProductRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class financialProductServiceImpl implements FinancialProductService {
    private final LocalDateTime now = LocalDateTime.now();

    private final FinancialProductRepository _repository;
    private final EntityManager entityManager;

    public financialProductServiceImpl(FinancialProductRepository repo, EntityManager entityManager) {
        _repository = repo;
        this.entityManager = entityManager;
    }

    public FinancialProduct createFinancialProduct(CreateFinancialProductDto fpDto) {

        Set<FinancialProductRelative> relatives = new HashSet<>();
        Long principalCustomerId = fpDto.relatives()
                .stream()
                .filter(CreateAccountRelativeDto::isPrincipal)
                .findFirst()
                .map(CreateAccountRelativeDto::customerId)
                .orElseThrow();


        FinancialProduct financialProduct = FinancialProduct.builder()
                .productType(fpDto.productType())
                .createdAt(now)
                .closedAt(null)
                .signType(fpDto.signType())
                .principalCustomerId(principalCustomerId)
                .productNumber(generateProductNumber(fpDto.productType()))
                .relatives(relatives)
                .build();

        for (CreateAccountRelativeDto relative : fpDto.relatives()) {
            FinancialProductRelative rl = new FinancialProductRelative();
            rl.setCustomerId(relative.customerId());
            rl.setPrincipal(true);
            rl.setFinancialProduct(financialProduct);
            rl.setRelationCondition(relative.accountRelatveCondition());
            relatives.add(rl);
        }
        //todo:Save in db to get id
      //  _repository.save(financialProduct);
        return financialProduct;
    }

    public String generateProductNumber(ProductType type) {
        Long seq;

        switch (type) {
            case ACCOUNT:
                seq = nextValue("seq_account_number");
                return "2" + String.format("%010d", seq);

            case LOAN:
                seq = nextValue("seq_loan_number");
                return "9" + String.format("%010d", seq);

            case CERTIFICATE_OF_DEPOSIT:
                seq = nextValue("seq_cd_number");
                return "7" + String.format("%010d", seq);

            default:
                throw new IllegalArgumentException("Unknown product type");
        }

    }

    public FinancialProduct getFinancialProductByProductNumber(String productNumber){
        return _repository.getByProductNumber(productNumber);
    }
    private Long nextValue(String sequenceName) {
        return ((Number) entityManager
                .createNativeQuery("SELECT "+sequenceName+".NEXTVAL FROM dual\n")
                .getSingleResult())
                .longValue();
    }
}
