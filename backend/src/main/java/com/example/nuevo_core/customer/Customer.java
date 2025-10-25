package com.example.nuevo_core.customer;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Customer {
    private long id;
    private char gender;
    private int age;
    private String documentType;
    private String documentId;
    private String address;
    private LocalDateTime createAt;
    @Nullable
    private Contacts contacts;
    private String status; //PUEDE SER NO APTO, MOROSO, ETC
}

@Data
@Builder
class Contacts{
    private long id;
    private long customerId;
    private List<PhoneNumber> phoneNumbers;

    @Nullable
    private List<Email> emails;
    private LocalDateTime updateAt;
}

@Data
@Builder
class PhoneNumber{
    private long id;
    private long customerId;
    private String number;
    private boolean isPrincipal;
    private LocalDateTime createdAt;
}

@Data
@Builder
class Email{
    private long id;
    private long customerId;
    private String email;
    private boolean isPrincipal;
}
