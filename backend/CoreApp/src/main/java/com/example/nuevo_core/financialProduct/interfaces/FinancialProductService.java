package com.example.nuevo_core.financialProduct.interfaces;

import com.example.nuevo_core.financialProduct.constants.ProductType;
import com.example.nuevo_core.financialProduct.dto.CreateFinancialProductDTO;
import com.example.nuevo_core.financialProduct.entity.FinancialProduct;

public interface FinancialProductService {
    FinancialProduct createFinancialProduct(CreateFinancialProductDTO fpDto);
    String generateProductNumber(ProductType type);
    void closeProduct(Long id);
}
