package ru.practicum.statsserverdto.dto;

public class StartEndValidationException extends RuntimeException {
    public StartEndValidationException(final String message) {
        super(message);
    }
}
