package ru.practicum.ewmserver.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewmserver.dto.LocationDto;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateEventRequest {
    @Size(min = 20, max = 2000)
    protected String annotation;
    protected Integer category;
    @Size(min = 20, max = 7000)
    protected String description;
    protected String eventDate;
    protected LocationDto location;
    protected Boolean paid;
    @PositiveOrZero
    protected Integer participantLimit;
    protected Boolean requestModeration;
    protected String stateAction;
    @Size(min = 3, max = 120)
    protected String title;
}
