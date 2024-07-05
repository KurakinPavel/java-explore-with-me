package ru.practicum.ewmserver.enums;

import java.util.Optional;

public enum UserStateAction {
    SEND_TO_REVIEW, CANCEL_REVIEW;

    public static Optional<UserStateAction> from(String stringStateAction) {
        for (UserStateAction stateAction : values()) {
            if (stateAction.name().equalsIgnoreCase(stringStateAction)) {
                return Optional.of(stateAction);
            }
        }
        return Optional.empty();
    }
}
