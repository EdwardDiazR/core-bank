package com.example.nuevo_core.customer.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
class PhoneNumber {
    private long id;
    private long customerId;
    private String number;
    private String category;
    private boolean isPrincipal;
    private LocalDateTime createdAt;
}
