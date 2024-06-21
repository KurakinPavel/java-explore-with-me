package ru.practicum.ewmserver.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    protected String annotation;
    protected Integer category;
    @NotBlank
    @Size(min = 20, max = 7000)
    protected String description;
    @NotBlank
    protected String eventDate;
    protected Location location;
    protected Boolean paid;
    protected int participantLimit;
    protected Boolean requestModeration;
    @NotBlank
    @Size(min = 3, max = 120)
    protected String title;
}
