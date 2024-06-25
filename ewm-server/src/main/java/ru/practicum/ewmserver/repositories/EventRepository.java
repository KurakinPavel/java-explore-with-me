package ru.practicum.ewmserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmserver.model.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
