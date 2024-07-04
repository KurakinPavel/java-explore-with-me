package ru.practicum.ewmserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmserver.model.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
