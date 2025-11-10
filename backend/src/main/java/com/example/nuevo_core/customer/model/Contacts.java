package com.example.nuevo_core.customer.model;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
class Contacts {

    private List<PhoneNumber> phoneNumbers;
    @Nullable
    private List<Email> emails;
}
