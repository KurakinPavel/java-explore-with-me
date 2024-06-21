package ru.practicum.ewmserver.dto;

public class EventShortDto {
    protected Integer id;
    protected String annotation;
    protected CategoryDto category;
    protected Integer confirmedRequests;
    protected String eventDate;
    protected UserShortDto initiator;
    protected Boolean paid;
    protected String title;
    protected int views;
}
