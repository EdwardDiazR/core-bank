package com.example.nuevo_core.customer.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PhoneNumber {
    private long id;
    private long customerId;
    private String number;
    private String category;
    private boolean isPrincipal;
    private LocalDateTime createdAt;
}
