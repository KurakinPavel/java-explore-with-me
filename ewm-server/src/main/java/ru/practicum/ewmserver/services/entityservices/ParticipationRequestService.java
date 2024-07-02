package ru.practicum.ewmserver.services.entityservices;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmserver.dto.ConfirmedRequestsStats;
import ru.practicum.ewmserver.dto.ParticipationRequestDto;
import ru.practicum.ewmserver.enums.ParticipationRequestStatus;
import ru.practicum.ewmserver.exceptions.custom.UserValidationException;
import ru.practicum.ewmserver.mappers.ParticipationRequestMapper;
import ru.practicum.ewmserver.model.Event;
import ru.practicum.ewmserver.model.ParticipationRequest;
import ru.practicum.ewmserver.model.User;
import ru.practicum.ewmserver.repositories.ParticipationRequestRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ParticipationRequestService {
    private final ParticipationRequestRepository participationRequestRepository;

    public ParticipationRequestDto save(User requester, Event event, ParticipationRequestStatus participationRequestStatus) {
        ParticipationRequest participationRequest = new ParticipationRequest(
                0,
                LocalDateTime.now(),
                event, requester,
                participationRequestStatus
        );
        log.info("Сохраняется запрос с параметрами: eventId={}, requesterId={}, status={}", event.getId(), requester.getId(), participationRequestStatus);
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
    }

    public void saveAll(List<ParticipationRequest> updatedParticipationRequests) {
        participationRequestRepository.saveAll(updatedParticipationRequests);
    }

    public ParticipationRequestDto cancel(int userId, int requestId) {
        ParticipationRequest canceledParticipationRequest = participationRequestRepository.getReferenceById(requestId);
        if (canceledParticipationRequest.getRequester().getId() != userId) {
            throw new UserValidationException("Нельзя отменять чужие запросы");
        }
        canceledParticipationRequest.setStatus(ParticipationRequestStatus.CANCELED);
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(canceledParticipationRequest));
    }

    public List<ParticipationRequestDto> getUserRequests(int userId) {
        return participationRequestRepository.findAllByRequester_Id(userId).stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    public List<ParticipationRequestDto> getRequestsForParticipationInUserEvent(int eventId) {
        return participationRequestRepository.findAllByEvent_Id(eventId).stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    public List<ParticipationRequest> getRequestByIds(List<Integer> requestIds) {
        return participationRequestRepository.findAllByIdIn(requestIds);
    }

    public ParticipationRequest getRequestByEventAndRequester(int eventId, int requesterId) {
        return participationRequestRepository.findOneByEvent_IdAndRequester_Id(eventId,requesterId);
    }
}
