package ru.practicum.ewmserver.enums;

import java.util.Optional;

public enum SortType {
    EVENT_DATE, VIEWS;

    public static Optional<SortType> from(String stringStateAction) {
        for (SortType sortType : values()) {
            if (sortType.name().equalsIgnoreCase(stringStateAction)) {
                return Optional.of(sortType);
            }
        }
        return Optional.empty();
    }
}
