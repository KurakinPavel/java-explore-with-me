package ru.practicum.ewmserver.enums;

import java.util.Optional;

public enum ParticipationRequestStatus {
    PENDING, CONFIRMED, REJECTED, CANCELED;

    public static Optional<ParticipationRequestStatus> from(String stringStatus) {
        for (ParticipationRequestStatus status : values()) {
            if (status.name().equalsIgnoreCase(stringStatus)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
