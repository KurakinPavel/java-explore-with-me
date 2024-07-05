package ru.practicum.ewmserver.exceptions.custom;

public class BadRequestValidationException extends RuntimeException {
    public BadRequestValidationException(final String message) {
        super(message);
    }
}
