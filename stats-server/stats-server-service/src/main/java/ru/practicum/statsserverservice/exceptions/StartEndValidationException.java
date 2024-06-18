package ru.practicum.statsserverservice.exceptions;

public class StartEndValidationException extends RuntimeException {
    public StartEndValidationException(final String message) {
        super(message);
    }
}
