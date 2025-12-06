package com.example.nuevo_core.financialProduct.dto;

import com.example.nuevo_core.financialProduct.constants.AccountSignType;
import com.example.nuevo_core.financialProduct.constants.ProductType;

import java.util.Set;

public record CreateFinancialProductDTO(ProductType productType,

                                        Set<CreateAccountRelativeDTO> relatives,
                                        AccountSignType signType) {
}
