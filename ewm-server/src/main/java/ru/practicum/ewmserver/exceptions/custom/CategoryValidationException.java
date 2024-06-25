package ru.practicum.ewmserver.exceptions.custom;

public class CategoryValidationException  extends RuntimeException {
    public CategoryValidationException(final String message) {
        super(message);
    }
}
