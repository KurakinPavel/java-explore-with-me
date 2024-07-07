package ru.practicum.ewmserver.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmserver.dto.event.EventFullDto;
import ru.practicum.ewmserver.dto.event.EventFullDtoWithRating;
import ru.practicum.ewmserver.dto.event.EventShotDtoWithRating;
import ru.practicum.ewmserver.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewmserver.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewmserver.dto.event.EventShortDto;
import ru.practicum.ewmserver.dto.event.NewEventDto;
import ru.practicum.ewmserver.dto.request.ParticipationRequestDto;
import ru.practicum.ewmserver.dto.event.UpdateEventRequest;
import ru.practicum.ewmserver.services.PrivateService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}")
@AllArgsConstructor
@Slf4j
public class PrivateController {
    private final PrivateService privateService;

    /** События */

    @GetMapping("/events")
    public List<EventShortDto> getUserEvents(@PathVariable @Positive Integer userId,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Запрос от private контроллера на получение списка событий, добавленных пользователем с Id={}", userId);
        return privateService.getUserEvents(userId, from, size);
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(@PathVariable Integer userId,
                             @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Запрос от private контроллера на публикацию нового события от пользователя с Id={}", userId);
        return privateService.saveEvent(userId, newEventDto);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getEventOfUser(@PathVariable @Positive Integer userId,
                                       @PathVariable @Positive Integer eventId) {
        log.info("Запрос от private контроллера на получение события с Id={}, добавленного пользователем с Id={}", eventId, userId);
        return privateService.getEventOfUser(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEventOfUser(@PathVariable Integer userId,
                               @PathVariable Integer eventId,
                               @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        log.info("Запрос от private контроллера от пользователя с id={} на обновление события с id={}", userId, eventId);
        return privateService.updateEventOfUser(userId, eventId, updateEventRequest);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForParticipationInUserEvent(
            @PathVariable @Positive Integer userId,
            @PathVariable @Positive Integer eventId) {
        log.info("Запрос от private контроллера на получение информации о запросах на участие в событии с id={} " +
                "пользователя с id={}", eventId, userId);
        return privateService.getRequestsForParticipationInUserEvent(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestsStatus(
            @PathVariable @Positive Integer userId,
            @PathVariable @Positive Integer eventId,
            @RequestBody @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Запрос от private контроллера на изменение статуса запросов на участие в событии с id={} " +
                "пользователя с id={}", eventId, userId);
        return privateService.updateRequestsStatus(userId, eventId, eventRequestStatusUpdateRequest);
    }

    /** Запросы на участие */

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable @Positive Integer userId) {
        log.info("Запрос от private контроллера на получение запросов пользователя с id={} на участие в чужих событиях ", userId);
        return privateService.getUserRequests(userId);
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto save(@PathVariable @Positive Integer userId,
                                        @RequestParam("eventId") @Positive Integer eventId) {
        log.info("Запрос от private контроллера на создание запроса на участие в событии: userId = {}, eventId = {}", userId, eventId);
        return privateService.saveParticipationRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable @Positive Integer userId,
                                          @PathVariable @Positive Integer requestId) {
        log.info("Запрос от private контроллера на отмену запроса с Id = {} пользователя с Id = {}", requestId, userId);
        return privateService.cancelParticipationRequest(userId, requestId);
    }

    /** Feature: Лайки и рейтинги */

    @PutMapping("/events/{eventId}/likes")
    public void putMark(@PathVariable @Positive Integer userId,
                        @PathVariable @Positive Integer eventId,
                        @RequestParam @NotNull Boolean score) {
        log.info("Запрос от private контроллера от пользователя с id={} на добавление реакции по событию с id={}", userId, eventId);
        privateService.putMark(userId, eventId, score);
    }

    @DeleteMapping("/events/{eventId}/likes")
    public void deleteMark(@PathVariable @Positive Integer userId,
                           @PathVariable @Positive Integer eventId) {
        log.info("Запрос от private контроллера от пользователя с id={} на удаление реакции по событию с id={}", userId, eventId);
        privateService.deleteMark(userId, eventId);
    }

    @GetMapping("/events/{eventId}/likes")
    public EventFullDtoWithRating getUserEventWithRating(@PathVariable @Positive Integer userId,
                                                     @PathVariable @Positive Integer eventId) {
        log.info("Запрос от private контроллера от пользователя с id={} на получение подробной информации о событии " +
                "с указанием рейтинга события и его инициатора", userId);
        return privateService.getUserEventWithRating(userId, eventId);
    }

    @GetMapping("/events/likes")
    public List<EventShotDtoWithRating> getEventsWithRating(@PathVariable @Positive Integer userId,
                                                      @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                      @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Запрос от private контроллера от пользователя с id={} на получение списка событий с наличием рейтинга " +
                "с указанием рейтингов событий и их инициаторов с сортировкой по убыванию рейтинга событий", userId);
        return privateService.getEventsWithRating(userId, from, size);
    }
}
