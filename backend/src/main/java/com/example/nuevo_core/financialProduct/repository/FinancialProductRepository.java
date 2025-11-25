package com.example.nuevo_core.financialProduct.repository;

import com.example.nuevo_core.financialProduct.entity.FinancialProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialProductRepository extends JpaRepository<FinancialProduct, Long> {
     FinancialProduct getByProductNumber(String productNumber);
}
