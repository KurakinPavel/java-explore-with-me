package ru.practicum.ewmserver.exceptions.custom;

public class EventValidationException extends RuntimeException {
    public EventValidationException(final String message) {
        super(message);
    }
}
