package com.example.nuevo_core.financialProduct.dto;

import com.example.nuevo_core.financialProduct.constants.AccountRelatveCondition;

public record CreateAccountRelativeDto(Long customerId,
                                       AccountRelatveCondition accountRelatveCondition,
                                       boolean isPrincipal) {
}
