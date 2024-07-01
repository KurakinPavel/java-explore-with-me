package ru.practicum.ewmserver.mappers;

import ru.practicum.ewmserver.dto.EventFullDto;
import ru.practicum.ewmserver.dto.EventShortDto;
import ru.practicum.ewmserver.dto.MomentFormatter;
import ru.practicum.ewmserver.dto.NewEventDto;
import ru.practicum.ewmserver.enums.EventState;
import ru.practicum.ewmserver.model.Category;
import ru.practicum.ewmserver.model.Event;
import ru.practicum.ewmserver.model.User;

import java.time.LocalDateTime;

public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto, User initiator, Category category) {
        return new Event(
                newEventDto.getId() != null ? newEventDto.getId() : 0,
                newEventDto.getAnnotation() != null ? newEventDto.getAnnotation() : "",
                category,
                LocalDateTime.now(),
                newEventDto.getDescription() != null ? newEventDto.getDescription() : "",
                newEventDto.getEventDate() != null ? LocalDateTime.parse(newEventDto.getEventDate(), MomentFormatter.DATE_TIME_FORMAT) : null,
                initiator,
                newEventDto.getLocation() != null ? newEventDto.getLocation() : null,
                newEventDto.getPaid() != null ? newEventDto.getPaid() : false,
                newEventDto.getParticipantLimit() != 0 ? newEventDto.getParticipantLimit() : 0,
                0,
                LocalDateTime.now(),
                newEventDto.getRequestModeration() != null ? newEventDto.getRequestModeration() : true,
                EventState.PENDING,
                newEventDto.getTitle() != null ? newEventDto.getTitle() : ""
        );
    }

    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn().format(MomentFormatter.DATE_TIME_FORMAT),
                event.getDescription(),
                event.getEventDate().format(MomentFormatter.DATE_TIME_FORMAT),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn().format(MomentFormatter.DATE_TIME_FORMAT),
                event.getRequestModeration(),
                event.getState().toString(),
                event.getTitle(),
                0
        );
    }

    public static EventShortDto fromFullToShortEventDTO(EventFullDto eventFullDto) {
        return new EventShortDto(
                eventFullDto.getId(),
                eventFullDto.getAnnotation(),
                eventFullDto.getCategory(),
                eventFullDto.getConfirmedRequests(),
                eventFullDto.getEventDate(),
                eventFullDto.getInitiator(),
                eventFullDto.getPaid(),
                eventFullDto.getTitle(),
                eventFullDto.getViews()
        );
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate().format(MomentFormatter.DATE_TIME_FORMAT),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                0
        );
    }
}
