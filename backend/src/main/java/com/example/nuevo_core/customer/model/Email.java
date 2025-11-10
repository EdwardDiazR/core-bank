package com.example.nuevo_core.customer.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class Email {
    private long id;
    private long customerId;
    private String email;
    private boolean isPrincipal;
}
