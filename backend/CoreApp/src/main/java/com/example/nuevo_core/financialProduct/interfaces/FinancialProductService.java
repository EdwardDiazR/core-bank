package com.example.nuevo_core.financialProduct.interfaces;

import com.example.nuevo_core.financialProduct.constants.ProductType;
import com.example.nuevo_core.financialProduct.dto.CreateFinancialProductDTO;
import com.example.nuevo_core.financialProduct.dto.SearchFinancialProductResponse;
import com.example.nuevo_core.financialProduct.entity.FinancialProduct;

import java.util.Set;

public interface FinancialProductService {
    FinancialProduct createFinancialProduct(CreateFinancialProductDTO fpDto);
    String generateProductNumber(ProductType type);
    void closeProduct(Long id);
    Set<SearchFinancialProductResponse> findFinancialProductByNumber(String searchValue);
}
