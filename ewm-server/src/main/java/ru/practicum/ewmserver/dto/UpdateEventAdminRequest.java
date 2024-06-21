package ru.practicum.ewmserver.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000)
    protected String annotation;
    protected Integer category;
    @Size(min = 20, max = 7000)
    protected String description;
    protected String eventDate;
    protected Location location;
    protected Boolean paid;
    protected Integer participantLimit;
    protected Boolean requestModeration;
    protected String stateAction;
    @Size(min = 3, max = 120)
    protected String title;
}
