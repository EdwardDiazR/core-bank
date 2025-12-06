package com.example.nuevo_core.account.dto;

import com.example.nuevo_core.account.constants.AccountCurrency;
import com.example.nuevo_core.financialProduct.constants.AccountSignType;
import com.example.nuevo_core.financialProduct.dto.CreateAccountRelativeDTO;
import com.example.nuevo_core.financialProduct.entity.FinancialProductRelative;

import java.util.Set;

public record CreateAccountDTO(
        AccountCurrency currency,
        AccountSignType signType,
        Set<CreateAccountRelativeDTO> relatives) {
}
