package ru.practicum.ewmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewmserver.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    protected Location location;
    protected Boolean paid;
    protected int participantLimit;
    protected Boolean requestModeration;
    @NotBlank
    @Size(min = 3, max = 120)
    protected String title;
}
