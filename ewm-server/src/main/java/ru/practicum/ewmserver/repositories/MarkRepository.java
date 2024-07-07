package ru.practicum.ewmserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmserver.model.Mark;

public interface MarkRepository extends JpaRepository<Mark, Integer> {

    Mark findOneByEvaluator_IdAndEvent_Id(int evaluatorId, int eventId);
}
