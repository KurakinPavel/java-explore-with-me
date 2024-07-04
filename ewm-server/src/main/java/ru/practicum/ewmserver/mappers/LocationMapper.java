package ru.practicum.ewmserver.mappers;

import ru.practicum.ewmserver.dto.LocationDto;
import ru.practicum.ewmserver.model.Location;

public class LocationMapper {

    public static Location toLocation(LocationDto locationDto) {
        return new Location(
                locationDto.getId() != null ? locationDto.getId() : 0,
                locationDto.getLat(),
                locationDto.getLon()
        );
    }

    public static LocationDto toLocationDto(Location location) {
        return new LocationDto(
                location.getId(),
                location.getLat(),
                location.getLon()
        );
    }
}
