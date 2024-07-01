package ru.practicum.ewmserver.exceptions.custom;

public class EventTimeValidationException extends RuntimeException {
    public EventTimeValidationException(final String message) {
        super(message);
    }
}
