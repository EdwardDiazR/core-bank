package com.example.nuevo_core.customer.entity;

import com.example.nuevo_core.customer.constants.DocumentType;
import com.example.nuevo_core.customer.dto.ContactsDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;
    private String documentId;
    private String nationality;
    private LocalDateTime createAt;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Address> addresses;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Email> emails;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PhoneNumber> phoneNumbers;
    private char gender;
    private int age;
    private String status; //PUEDE SER NO APTO, MOROSO, ETC
    private boolean isPEP;
    private boolean isUnderAge;
    private boolean isEmployee;
}

