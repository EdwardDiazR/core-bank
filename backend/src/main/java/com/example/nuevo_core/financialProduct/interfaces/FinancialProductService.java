package com.example.nuevo_core.financialProduct.interfaces;

import com.example.nuevo_core.financialProduct.constants.ProductType;
import com.example.nuevo_core.financialProduct.dto.CreateFinancialProductDto;
import com.example.nuevo_core.financialProduct.entity.FinancialProduct;

public interface FinancialProductService {
    FinancialProduct createFinancialProduct(CreateFinancialProductDto fpDto);
    String generateProductNumber(ProductType type);
}
