package com.example.nuevo_core.customer.model;

import com.example.nuevo_core.customer.constants.DocumentType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Customer {
    private long id;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;
    private String documentId;
    private String nationality;
    private List<Address> addresses;
    private LocalDateTime createAt;
    private Contacts contacts;
    private char gender;
    private int age;
    private String status; //PUEDE SER NO APTO, MOROSO, ETC
}

