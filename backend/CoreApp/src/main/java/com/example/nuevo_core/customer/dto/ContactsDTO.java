package com.example.nuevo_core.customer.dto;

import com.example.nuevo_core.customer.entity.Address;
import com.example.nuevo_core.customer.entity.Email;
import com.example.nuevo_core.customer.entity.PhoneNumber;

import java.util.Set;


public record ContactsDTO(Set<Address> addresses,
                          Set<Email> emails,
                          Set<PhoneNumber> phoneNumbers) {
};

