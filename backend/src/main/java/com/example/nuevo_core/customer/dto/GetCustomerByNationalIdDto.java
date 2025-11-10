package com.example.nuevo_core.customer.dto;

import com.example.nuevo_core.customer.constants.DocumentType;

public record GetCustomerByNationalIdDto(String nationalId, DocumentType documentType) {
}
