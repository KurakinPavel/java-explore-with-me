package ru.practicum.ewmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewmserver.model.Location;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    protected Integer id;
    protected String annotation;
    protected CategoryDto category;
    protected Integer confirmedRequests;
    protected String createdOn;
    protected String description;
    protected String eventDate;
    protected UserShortDto initiator;
    protected Location location;
    protected Boolean paid;
    protected int participantLimit;
    protected String publishedOn;
    protected Boolean requestModeration;
    protected String state;
    protected String title;
    protected int views;
}
