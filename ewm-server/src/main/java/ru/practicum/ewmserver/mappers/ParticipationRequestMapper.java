package ru.practicum.ewmserver.mappers;

import ru.practicum.ewmserver.dto.request.ParticipationRequestDto;
import ru.practicum.ewmserver.model.ParticipationRequest;

public class ParticipationRequestMapper {

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
