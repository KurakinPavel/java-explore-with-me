package ru.practicum.ewmserver.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmserver.dto.EventFullDto;
import ru.practicum.ewmserver.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewmserver.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewmserver.dto.EventShortDto;
import ru.practicum.ewmserver.dto.NewEventDto;
import ru.practicum.ewmserver.dto.ParticipationRequestDto;
import ru.practicum.ewmserver.dto.UpdateEventRequest;
import ru.practicum.ewmserver.services.PrivateService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}")
@AllArgsConstructor
@Slf4j
public class PrivateController {
    private final PrivateService privateService;

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto save(@PathVariable Integer userId,
                             @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Request from controller for post new Event: userId = {}", userId);
        return privateService.save(userId, newEventDto);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto update(@PathVariable Integer userId,
                               @PathVariable Integer eventId,
                               @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        log.info("Request from User id = {} for update Event id = {}", userId, eventId);
        return privateService.update(userId, eventId, updateEventRequest);
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto save(@PathVariable @Positive Integer userId,
                                        @RequestParam("eventId") @Positive Integer eventId) {
        log.info("Запрос от контроллера на создание запроса на участие в событии: userId = {}, eventId = {}", userId, eventId);
        return privateService.save(userId, eventId);
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable @Positive Integer userId) {
        return privateService.getUserRequests(userId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestsStatus(
            @PathVariable @Positive Integer userId,
            @PathVariable @Positive Integer eventId,
            @RequestBody @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return privateService.updateRequestsStatus(userId, eventId, eventRequestStatusUpdateRequest);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForParticipationInUserEvent(
            @PathVariable @Positive Integer userId,
            @PathVariable @Positive Integer eventId) {
        return privateService.getRequestsForParticipationInUserEvent(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable @Positive Integer userId,
                                          @PathVariable @Positive Integer requestId) {
        return privateService.cancel(userId, requestId);
    }

    @GetMapping("/events")
    public List<EventShortDto> getUserEvents(@PathVariable @Positive Integer userId,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        return privateService.getUserEvents(userId, from, size);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getEventOfUser(@PathVariable @Positive Integer userId,
                                       @PathVariable @Positive Integer eventId) {
        return privateService.getEventOfUser(userId, eventId);
    }
}
