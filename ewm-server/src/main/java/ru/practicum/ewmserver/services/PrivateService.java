package ru.practicum.ewmserver.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmserver.dto.event.EventFullDto;
import ru.practicum.ewmserver.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewmserver.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewmserver.dto.event.EventShortDto;
import ru.practicum.ewmserver.dto.MomentFormatter;
import ru.practicum.ewmserver.dto.event.NewEventDto;
import ru.practicum.ewmserver.dto.request.ParticipationRequestDto;
import ru.practicum.ewmserver.dto.event.UpdateEventRequest;
import ru.practicum.ewmserver.enums.EventState;
import ru.practicum.ewmserver.enums.ParticipationRequestStatus;
import ru.practicum.ewmserver.exceptions.custom.EventTimeValidationException;
import ru.practicum.ewmserver.exceptions.custom.ParticipationRequestValidationException;
import ru.practicum.ewmserver.exceptions.custom.UserValidationException;
import ru.practicum.ewmserver.mappers.ParticipationRequestMapper;
import ru.practicum.ewmserver.mappers.UserMapper;
import ru.practicum.ewmserver.model.Category;
import ru.practicum.ewmserver.model.Event;
import ru.practicum.ewmserver.model.ParticipationRequest;
import ru.practicum.ewmserver.model.User;
import ru.practicum.ewmserver.services.entityservices.CategoryService;
import ru.practicum.ewmserver.services.entityservices.EventService;
import ru.practicum.ewmserver.services.entityservices.ParticipationRequestService;
import ru.practicum.ewmserver.services.entityservices.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class PrivateService {
    private final EventService eventService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ParticipationRequestService participationRequestService;

    /** События */

    @Transactional(readOnly = true)
    public List<EventShortDto> getUserEvents(int userId, int from, int size) {
        userService.getUser(userId).getId();
        return eventService.getUserEvents(userId, from, size);
    }

    @Transactional
    public EventFullDto saveEvent(int userId, NewEventDto newEventDto) {
        if (LocalDateTime.parse(newEventDto.getEventDate(), MomentFormatter.DATE_TIME_FORMAT)
                .isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EventTimeValidationException("Дата события не может быть раньше, чем через два часа от текущего момента");
        }
        User initiator = userService.getUser(userId);
        Category category = categoryService.getCategory(newEventDto.getCategory());
        return eventService.save(newEventDto, initiator, category);
    }

    @Transactional(readOnly = true)
    public EventFullDto getEventOfUser(int userId, int eventId) {
        User user = userService.getUser(userId);
        UserMapper.toUserDto(user);
        return eventService.getEventOfUserForPrivate(userId, eventId);
    }

    @Transactional
    public EventFullDto updateEventOfUser(int userId, int eventId, UpdateEventRequest updateEventRequest) {
        Category category = null;
        if (updateEventRequest.getCategory() != null) {
            category = categoryService.getCategory(updateEventRequest.getCategory());
        }
        return eventService.updateByUser(userId, eventId, updateEventRequest, category);
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsForParticipationInUserEvent(int userId, int eventId) {
        int initiatorId = eventService.getEvent(eventId).getInitiator().getId();
        if (userId != initiatorId) {
            throw new UserValidationException("Событий пользователя не найдено");
        }
        return participationRequestService.getRequestsForParticipationInUserEvent(eventId);
    }

    @Transactional
    public EventRequestStatusUpdateResult updateRequestsStatus(int userId, int eventId,
                                                               EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        userService.getUser(userId).getId();
        Event event = eventService.getEvent(eventId);
        List<Integer> requestIds = eventRequestStatusUpdateRequest.getRequestIds();
        List<ParticipationRequest> participationRequests = participationRequestService.getRequestByIds(requestIds);
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            event.setConfirmedRequests(event.getConfirmedRequests() + participationRequests.size());
            eventService.save(event);
            List<ParticipationRequestDto> participationRequestsDto = participationRequests.stream()
                    .map(ParticipationRequestMapper::toParticipationRequestDto)
                    .collect(Collectors.toList());
            return new EventRequestStatusUpdateResult(participationRequestsDto, new ArrayList<>());
        }
        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new ParticipationRequestValidationException("Достигнут предел количества участников. Заявки отклонены");
        }
        List<ParticipationRequest> updatedParticipationRequests = new ArrayList<>();
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        for (ParticipationRequest participationRequest : participationRequests) {
            if (!participationRequest.getStatus().equals(ParticipationRequestStatus.PENDING)) {
                throw new ParticipationRequestValidationException("Статус заявок можно изменить только у " +
                        "находящихся в режиме ожидания. Заявки отклонены");
            }
            if (event.getParticipantLimit() > event.getConfirmedRequests() &&
                    ParticipationRequestStatus.from(eventRequestStatusUpdateRequest.getStatus()).orElseThrow(() ->
                                    new IllegalArgumentException("Unknown state: " + eventRequestStatusUpdateRequest.getStatus()))
                            .equals(ParticipationRequestStatus.CONFIRMED)) {
                participationRequest.setStatus(ParticipationRequestStatus.CONFIRMED);
                updatedParticipationRequests.add(participationRequest);
                confirmedRequests.add(ParticipationRequestMapper.toParticipationRequestDto(participationRequest));
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            } else {
                participationRequest.setStatus(ParticipationRequestStatus.REJECTED);
                updatedParticipationRequests.add(participationRequest);
                rejectedRequests.add(ParticipationRequestMapper.toParticipationRequestDto(participationRequest));
            }
        }
        eventService.save(event);
        participationRequestService.saveAll(updatedParticipationRequests);
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    /** Запросы на участие */

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getUserRequests(int userId) {
        userService.getUser(userId).getId();
        return participationRequestService.getUserRequests(userId);
    }

    @Transactional
    public ParticipationRequestDto saveParticipationRequest(int requesterId, int eventId) {
        User requester = userService.getUser(requesterId);
        Event event = eventService.getEvent(eventId);
        if (requesterId == event.getInitiator().getId()) {
            throw new ParticipationRequestValidationException("Инициатор не может делать запрос на участие в своём событии");
        }
        ParticipationRequest participationRequest = participationRequestService.getRequestByEventAndRequester(eventId, requesterId);
        if (participationRequest != null) {
            throw new ParticipationRequestValidationException("Повторный запрос подать нельзя");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ParticipationRequestValidationException("Событие не опубликовано");
        }
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new ParticipationRequestValidationException("Достигнут предел количества участников");
        }
        ParticipationRequestStatus participationRequestStatus = ParticipationRequestStatus.PENDING;
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            participationRequestStatus = ParticipationRequestStatus.CONFIRMED;
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventService.save(event);
        }
        return participationRequestService.save(requester, event, participationRequestStatus);
    }

    @Transactional
    public ParticipationRequestDto cancelParticipationRequest(int userId, int requestId) {
        return participationRequestService.cancel(userId, requestId);
    }
}
