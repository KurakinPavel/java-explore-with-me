package ru.practicum.ewmserver.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmserver.dto.EventFullDto;
import ru.practicum.ewmserver.dto.EventShortDto;
import ru.practicum.ewmserver.dto.NewEventDto;
import ru.practicum.ewmserver.dto.ParticipationRequestDto;
import ru.practicum.ewmserver.dto.UpdateEventRequest;
import ru.practicum.ewmserver.model.Category;
import ru.practicum.ewmserver.model.Event;
import ru.practicum.ewmserver.model.User;
import ru.practicum.ewmserver.services.entityservices.CategoryService;
import ru.practicum.ewmserver.services.entityservices.EventService;
import ru.practicum.ewmserver.services.entityservices.ParticipationRequestService;
import ru.practicum.ewmserver.services.entityservices.UserService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PrivateService {
    private final EventService eventService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ParticipationRequestService participationRequestService;

    @Transactional
    public EventFullDto save(int userId, NewEventDto newEventDto) {
        User initiator = userService.getUser(userId);
        Category category = categoryService.getCategory(newEventDto.getCategory());
        return eventService.save(newEventDto, initiator, category);
    }

    @Transactional
    public EventFullDto update(int userId, int eventId, UpdateEventRequest updateEventRequest) {
        Category category = null;
        if (updateEventRequest.getCategory() != null) {
            category = categoryService.getCategory(updateEventRequest.getCategory());
        }
        return eventService.updateByUser(userId, eventId, updateEventRequest, category);
    }

    @Transactional
    public ParticipationRequestDto save(int requesterId, int eventId) {
        User requester = userService.getUser(requesterId);
        Event event = eventService.getEvent(eventId);
        return participationRequestService.save(requester, event);
    }

    @Transactional
    public ParticipationRequestDto cancel(int userId, int requestId) {
        return participationRequestService.cancel(userId, requestId);
    }

    @Transactional(readOnly = true)
    public List<EventShortDto> getUserEvents(int userId, int from, int size) {
        User user = userService.getUser(userId);
        return eventService.getUserEvents(userId, from, size);
    }

}
