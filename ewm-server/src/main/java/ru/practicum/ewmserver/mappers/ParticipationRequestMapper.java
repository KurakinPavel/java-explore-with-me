package ru.practicum.ewmserver.mappers;

import ru.practicum.ewmserver.dto.ParticipationRequestDto;
import ru.practicum.ewmserver.enums.ParticipationRequestStatus;
import ru.practicum.ewmserver.model.Event;
import ru.practicum.ewmserver.model.ParticipationRequest;
import ru.practicum.ewmserver.model.User;

import java.time.LocalDateTime;

public class ParticipationRequestMapper {

    public static ParticipationRequest toParticipationRequest(User requester, Event event, ParticipationRequestDto participationRequestDto) {
        return new ParticipationRequest(
                participationRequestDto.getId() != null ? participationRequestDto.getId() : 0,
                LocalDateTime.now(),
                event, requester,
                ParticipationRequestStatus.from(participationRequestDto.getStatus()).orElseThrow(() ->
                        new IllegalArgumentException("Unknown state: " + participationRequestDto.getStatus()))
        );
    }

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return new ParticipationRequestDto(
                participationRequest.getId(),
                participationRequest.getRequester().getId(),
                participationRequest.getCreated().toString(),
                participationRequest.getEvent().getId(),
                participationRequest.getStatus().toString()
        );
    }
}
