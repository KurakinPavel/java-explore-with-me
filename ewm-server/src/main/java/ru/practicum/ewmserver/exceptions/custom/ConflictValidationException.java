package ru.practicum.ewmserver.exceptions.custom;

public class ConflictValidationException extends RuntimeException {
    public ConflictValidationException(final String message) {
        super(message);
    }
}
