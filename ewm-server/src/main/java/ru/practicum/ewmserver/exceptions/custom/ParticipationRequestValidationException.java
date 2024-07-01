package ru.practicum.ewmserver.exceptions.custom;

public class ParticipationRequestValidationException extends RuntimeException {
    public ParticipationRequestValidationException(final String message) {
        super(message);
    }
}
