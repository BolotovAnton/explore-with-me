package ru.practicum.main_service.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final String status;

    private final String reason;

    private final String message;

    private final String timestamp;
}

