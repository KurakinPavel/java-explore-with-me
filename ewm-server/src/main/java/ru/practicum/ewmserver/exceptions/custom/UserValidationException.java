package ru.practicum.ewmserver.exceptions.custom;

public class UserValidationException extends RuntimeException {
    public UserValidationException(final String message) {
        super(message);
    }
}
