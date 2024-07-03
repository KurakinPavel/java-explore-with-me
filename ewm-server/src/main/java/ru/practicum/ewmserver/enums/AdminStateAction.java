package ru.practicum.ewmserver.enums;

import java.util.Optional;

public enum AdminStateAction {
    PUBLISH_EVENT, REJECT_EVENT;

    public static Optional<AdminStateAction> from(String stringStateAction) {
        for (AdminStateAction stateAction : values()) {
            if (stateAction.name().equalsIgnoreCase(stringStateAction)) {
                return Optional.of(stateAction);
            }
        }
        return Optional.empty();
    }
}
