package ru.practicum.ewmserver.services.entityservices;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmserver.model.Location;
import ru.practicum.ewmserver.repositories.LocationRepository;

@Slf4j
@Service
@AllArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }
}
