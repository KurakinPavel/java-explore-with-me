package ru.practicum.ewmserver.model;

import ru.practicum.ewmserver.dto.LocationDto;
import ru.practicum.ewmserver.enums.EventState;

import java.time.LocalDateTime;

public class Event {
    protected int id;
    protected String annotation;
    protected Category category;
    protected LocalDateTime createsOn;
    protected String description;
    protected LocalDateTime eventDate;
    protected User initiator;
    protected EventLocation location;
    protected Boolean paid;
    protected int participantLimit;
    protected LocalDateTime publishedOn;
    protected Boolean requestModeration;
    protected EventState state;
    protected String title;
}
