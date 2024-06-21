package ru.practicum.ewmserver.dto;

import ru.practicum.ewmserver.dto.ParticipationRequestDto;

import java.util.List;

public class EventRequestStatusUpdateResult {
    protected List<ParticipationRequestDto> confirmedRequests;
    protected List<ParticipationRequestDto> rejectedRequests;
}
