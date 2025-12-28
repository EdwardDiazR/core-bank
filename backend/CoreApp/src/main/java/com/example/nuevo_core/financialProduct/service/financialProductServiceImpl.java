package com.example.nuevo_core.financialProduct.service;

import com.example.nuevo_core.financialProduct.constants.AccountSignType;
import com.example.nuevo_core.financialProduct.constants.ProductType;
import com.example.nuevo_core.financialProduct.dto.CreateAccountRelativeDTO;
import com.example.nuevo_core.financialProduct.dto.CreateFinancialProductDTO;
import com.example.nuevo_core.financialProduct.entity.FinancialProductRelative;
import com.example.nuevo_core.financialProduct.entity.FinancialProduct;
import com.example.nuevo_core.financialProduct.exceptions.DuplicatedRelativeException;
import com.example.nuevo_core.financialProduct.interfaces.FinancialProductService;
import com.example.nuevo_core.financialProduct.repository.FinancialProductRepository;
import com.example.nuevo_core.loan.exceptions.InvalidSignTypeException;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class financialProductServiceImpl implements FinancialProductService {
    private final LocalDateTime now = LocalDateTime.now();

    private final FinancialProductRepository _repository;
    private final EntityManager entityManager;

    public financialProductServiceImpl(FinancialProductRepository repo, EntityManager entityManager) {
        _repository = repo;
        this.entityManager = entityManager;
    }

    @Override
    public FinancialProduct createFinancialProduct(CreateFinancialProductDTO financialProductDTO) {

        if (financialProductDTO.relatives() == null || financialProductDTO.relatives().isEmpty()) {
            throw new RuntimeException("No hay relacionados");
        }
        int relativesQuantity = financialProductDTO.relatives().size();

        if (financialProductDTO.signType() == AccountSignType.UNIQUE && relativesQuantity > 1) {
            throw new InvalidSignTypeException("La condicion de firma no puede ser unica si hay mas de un firmante");
        }

        if (financialProductDTO.signType() != AccountSignType.UNIQUE && relativesQuantity < 2) {
            throw new InvalidSignTypeException("La condicion de firma requiere mas de un firmante");
        }

        if (financialProductDTO.relatives().stream().filter(CreateAccountRelativeDTO::isPrincipal).count() > 1) {
            throw new InvalidSignTypeException("Solo esta permitido un solo firmante principal por producto");
        }

        Set<FinancialProductRelative> relatives = new HashSet<>();

        Long principalCustomerId = financialProductDTO.relatives()
                .stream()
                .filter(CreateAccountRelativeDTO::isPrincipal)
                .findFirst()
                .map(CreateAccountRelativeDTO::customerId)
                .orElseThrow(() -> new InvalidSignTypeException("Debes agregar un firmante principal"));


        Long generatedProductNumber = generateProductNumberV2();
        String productNumber;
        if (financialProductDTO.productType() == ProductType.CREDIT_CARD) {
            productNumber = "CC_NUMBER PAN OR BIN"; //todo: Asign CREDIT CARD PRODUCT_NUMBER
        } else {
            productNumber = String.valueOf(generatedProductNumber);

        }

        String regionalProductNumber = "BANK-" + String.format("%020d", generatedProductNumber);

        FinancialProduct financialProduct = FinancialProduct.builder()
                .productType(financialProductDTO.productType())
                .createdAt(LocalDateTime.now())
                .closedAt(null)
                .signType(financialProductDTO.signType())
                .principalCustomerId(principalCustomerId)
                .productNumber(productNumber)
                .regionalProductNumber(regionalProductNumber)
                .relatives(relatives)
                .transactions(null)
                .build();

        Set<CreateAccountRelativeDTO> relativesExcludingPrincipal = financialProductDTO.relatives().stream().filter(r -> !r.isPrincipal()).collect(Collectors.toSet());
        for (CreateAccountRelativeDTO relative : relativesExcludingPrincipal) {

            boolean relativeExist = !relatives.isEmpty() && relatives
                    .stream()
                    .anyMatch(r -> r.getCustomerId().equals(relative.customerId()));

            if (relativeExist) {
                throw new DuplicatedRelativeException("Ya existe este cliente como firmante");
            }

            FinancialProductRelative productRelative = new FinancialProductRelative();
            productRelative.setCustomerId(relative.customerId());
            productRelative.setPrincipal(relative.isPrincipal());
            productRelative.setFinancialProduct(financialProduct);
            productRelative.setRelationCondition(relative.accountRelatveCondition());
            relatives.add(productRelative);
        }
        //todo:Save in db to get id
        _repository.save(financialProduct);
        return financialProduct;
    }

    //Not used
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

    public Long generateProductNumberV2() {
        return nextValue("product_global_seq");
    }


    public FinancialProduct getFinancialProductByProductNumber(String productNumber) {
        return _repository.getByProductNumber(productNumber);
    }

    private Long nextValue(String sequenceName) {

        return ((Number) entityManager
                .createNativeQuery("SELECT " + sequenceName + ".NEXTVAL FROM dual\n")
                .getSingleResult())
                .longValue();
    }

    @Override
    public void closeProduct(Long id) {

    }
}
