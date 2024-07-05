package ru.practicum.ewmserver.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    protected Integer id;
    protected Double lat;
    protected Double lon;
}
