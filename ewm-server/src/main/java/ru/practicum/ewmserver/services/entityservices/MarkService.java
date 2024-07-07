package ru.practicum.ewmserver.services.entityservices;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmserver.model.Mark;
import ru.practicum.ewmserver.repositories.MarkRepository;

@Slf4j
@Service
@AllArgsConstructor
public class MarkService {
    private final MarkRepository markRepository;

    @Transactional
    public void saveMark(Mark mark) {
        markRepository.save(mark);
    }

    @Transactional
    public void deleteMark(Mark mark) {
        markRepository.delete(mark);
    }

    @Transactional(readOnly = true)
    public Mark findMarkByEvaluatorAndEvent(int evaluatorId, int eventId) {
        return markRepository.findOneByEvaluator_IdAndEvent_Id(evaluatorId, eventId);
    }
}
