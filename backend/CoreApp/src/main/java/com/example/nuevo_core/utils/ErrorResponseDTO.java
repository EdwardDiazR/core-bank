package com.example.nuevo_core.utils;

import java.time.LocalDateTime;

public record ErrorResponseDTO(int status,
                               String message,
                               LocalDateTime time) {
}
