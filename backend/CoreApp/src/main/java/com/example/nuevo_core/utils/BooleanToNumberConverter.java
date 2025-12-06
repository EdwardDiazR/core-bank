package com.example.nuevo_core.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BooleanToNumberConverter implements AttributeConverter<Boolean,Integer> {

    @Override
    public Integer convertToDatabaseColumn(Boolean value) {
        return (value != null && value) ? 1 : 0;
    }

    @Override
    public Boolean convertToEntityAttribute(Integer dbValue) {
        return dbValue != null && dbValue == 1;
    }
}
