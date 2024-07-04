package ru.practicum.ewmserver.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewmserver.dto.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewEventDto {
    protected Integer id;
    @NotBlank
    @Size(min = 20, max = 2000)
    protected String annotation;
    protected Integer category;
    @NotBlank
    @Size(min = 20, max = 7000)
    protected String description;
    @NotBlank
    protected String eventDate;
    @NotNull
    protected LocationDto location;
    protected Boolean paid;
    @PositiveOrZero
    protected int participantLimit;
    protected Boolean requestModeration;
    @NotBlank
    @Size(min = 3, max = 120)
    protected String title;
}
