package ru.practicum.main_service.exception;

public class WrongTermsException extends RuntimeException {
    public WrongTermsException(String message) {
        super(message);
    }
}
