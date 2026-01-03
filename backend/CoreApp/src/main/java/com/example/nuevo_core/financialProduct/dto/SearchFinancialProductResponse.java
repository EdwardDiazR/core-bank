package com.example.nuevo_core.financialProduct.dto;

import com.example.nuevo_core.financialProduct.constants.ProductType;

public record SearchFinancialProductResponse(
        String productNumber,
        String publicId,
        ProductType productType
) {
}
