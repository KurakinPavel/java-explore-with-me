package ru.practicum.ewmserver.services.entityservices;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmserver.dto.ParticipationRequestDto;
import ru.practicum.ewmserver.enums.ParticipationRequestStatus;
import ru.practicum.ewmserver.exceptions.custom.UserValidationException;
import ru.practicum.ewmserver.mappers.ParticipationRequestMapper;
import ru.practicum.ewmserver.model.Event;
import ru.practicum.ewmserver.model.ParticipationRequest;
import ru.practicum.ewmserver.model.User;
import ru.practicum.ewmserver.repositories.ParticipationRequestRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class ParticipationRequestService {
    private final ParticipationRequestRepository participationRequestRepository;

    public ParticipationRequestDto save(User requester, Event event) {
        ParticipationRequest participationRequest = new ParticipationRequest(
                0,
                LocalDateTime.now(),
                event, requester,
                ParticipationRequestStatus.PENDING
        );
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
    }

    public ParticipationRequestDto cancel(int userId, int requestId) {
        ParticipationRequest canceledParticipationRequest = participationRequestRepository.getReferenceById(requestId);
        if (canceledParticipationRequest.getRequester().getId() != userId) {
            throw new UserValidationException("Нельзя отменять чужие запросы");
        }
        canceledParticipationRequest.setStatus(ParticipationRequestStatus.REJECTED);
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(canceledParticipationRequest));
    }
}
