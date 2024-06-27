package ru.practicum.ewmserver.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmserver.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    Page<Event> findAllByInitiator_Id(int userId, Pageable pageable);

}
