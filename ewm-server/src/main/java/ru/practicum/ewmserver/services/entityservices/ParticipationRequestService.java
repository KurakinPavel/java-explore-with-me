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
        canceledParticipationRequest.setStatus(ParticipationRequestStatus.REJECTED);
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(canceledParticipationRequest));
    }

    public List<ParticipationRequest> getRequestByEventAndRequestIds(List<Integer> requestIds) {
        return participationRequestRepository.findAllByIdIn(requestIds);
    }

    public Map<Integer, Integer> countEventsConfirmedRequests(List<Integer> eventIds) {
        List<ConfirmedRequestsStats> statsForConfirmedRequests = participationRequestRepository.findStatsForRequests(ParticipationRequestStatus.CONFIRMED, eventIds);
        Map<Integer, Integer> eventsConfirmedRequests = new HashMap<>();
        for (ConfirmedRequestsStats stats : statsForConfirmedRequests) {
            eventsConfirmedRequests.put(stats.getEventId(), Math.toIntExact(stats.getConfirmedRequests()));
        }
        return eventsConfirmedRequests;
    }

    public ParticipationRequest getRequestByEventAndRequester(int eventId, int requesterId) {
        return participationRequestRepository.findOneByEvent_IdAndRequester_Id(eventId,requesterId);
    }
}
