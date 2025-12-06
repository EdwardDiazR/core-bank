package com.example.nuevo_core.utils;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Type;
import java.time.LocalDateTime;


public record ApiResponse(
        boolean success,
        String message,
        int statusCode,
        @Nullable Object data,
        LocalDateTime time
) {
}
