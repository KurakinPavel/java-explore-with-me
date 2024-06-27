package ru.practicum.ewmserver.services.entityservices;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ewmserver.dto.EventFullDto;
import ru.practicum.ewmserver.dto.EventShortDto;
import ru.practicum.ewmserver.dto.MomentFormatter;
import ru.practicum.ewmserver.dto.NewEventDto;
import ru.practicum.ewmserver.dto.UpdateEventRequest;
import ru.practicum.ewmserver.enums.AdminStateAction;
import ru.practicum.ewmserver.enums.EventState;
import ru.practicum.ewmserver.enums.UserStateAction;
import ru.practicum.ewmserver.exceptions.custom.GetStatisticException;
import ru.practicum.ewmserver.exceptions.custom.UserValidationException;
import ru.practicum.ewmserver.mappers.EventMapper;
import ru.practicum.ewmserver.model.Category;
import ru.practicum.ewmserver.model.Event;
import ru.practicum.ewmserver.model.User;
import ru.practicum.ewmserver.repositories.EventRepository;
import ru.practicum.statserverclient.client.StatsServerClient;
import ru.practicum.statsserverdto.dto.StatsDtoOut;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final StatsServerClient statsServerClient;

    public EventFullDto save(NewEventDto newEventDto, User initiator, Category category) {
        return EventMapper.toEventFullDto(eventRepository.save(EventMapper.toEvent(newEventDto, initiator, category)));
    }

    public EventFullDto updateByAdmin(int eventId, UpdateEventRequest updateEventRequest, Category category) {
        Event updatingEvent = getEvent(eventId);
        if (updateEventRequest.getStateAction() != null) {
            AdminStateAction adminStateAction = AdminStateAction.from(updateEventRequest.getStateAction()).orElseThrow(() ->
                    new IllegalArgumentException("Unknown state: " + updateEventRequest.getStateAction()));
            if (adminStateAction.equals(AdminStateAction.PUBLISH_EVENT)) {
                updatingEvent.setState(EventState.PUBLISHED);
            } else {
                updatingEvent.setState(EventState.CANCELED);
            }
        }
        return update(updatingEvent, updateEventRequest, category);
    }

    public EventFullDto updateByUser(int userId, int eventId, UpdateEventRequest updateEventRequest, Category category) {
        Event updatingEvent = getEvent(eventId);
        if (updatingEvent.getInitiator().getId() != userId) {
            throw new UserValidationException("Событие может редактировать только инициатор");
        }
        if (updateEventRequest.getStateAction() != null) {
            UserStateAction userStateAction = UserStateAction.from(updateEventRequest.getStateAction()).orElseThrow(() ->
                    new IllegalArgumentException("Unknown state: " + updateEventRequest.getStateAction()));
            if (userStateAction.equals(UserStateAction.SEND_TO_REVIEW)) {
                updatingEvent.setState(EventState.PENDING);
            } else {
                updatingEvent.setState(EventState.CANCELED);
            }
        }
        return update(updatingEvent, updateEventRequest, category);
    }

    private EventFullDto update(Event updatingEvent, UpdateEventRequest updateEventRequest, Category category) {
        if (updateEventRequest.getAnnotation() != null) {
            updatingEvent.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            updatingEvent.setCategory(category);
        }
        if (updateEventRequest.getDescription() != null) {
            updatingEvent.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getEventDate() != null) {
            updatingEvent.setEventDate(LocalDateTime.parse(updateEventRequest.getEventDate(), MomentFormatter.DATE_TIME_FORMAT));
        }
        if (updateEventRequest.getLocation() != null) {
            updatingEvent.setLocation(updateEventRequest.getLocation());
        }
        if (updateEventRequest.getPaid() != null) {
            updatingEvent.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            updatingEvent.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getRequestModeration() != null) {
            updatingEvent.setRequestModeration(updateEventRequest.getRequestModeration());
        }
        if (updateEventRequest.getTitle() != null) {
            updatingEvent.setTitle(updateEventRequest.getTitle());
        }
        return EventMapper.toEventFullDto(eventRepository.save(updatingEvent));
    }

    public List<EventShortDto> getUserEvents(int userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Event> events;
        events = eventRepository.findAllByInitiator_Id(userId, pageable);
        List<EventShortDto> shortEventsDto = events
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
        List<String> uris = new ArrayList<>();
        for (Event event : events) {
            uris.add("/events/" + event.getId());
        }
        String start = LocalDateTime.now().minusYears(10).format(MomentFormatter.DATE_TIME_FORMAT);
        String end = LocalDateTime.now().plusYears(10).format(MomentFormatter.DATE_TIME_FORMAT);
        log.info("Передаю: {}, {}", start, end);
        ResponseEntity<Object> responseEntity = statsServerClient.getHitsStatistics(start, end, uris, true);
        Map<Integer, Integer> statistic = new HashMap<>();
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            try {
                List<StatsDtoOut> stats = (List<StatsDtoOut>) responseEntity.getBody();
                assert stats != null;
                for (StatsDtoOut statsDtoOut : stats) {
                    statistic.put(Integer.parseInt(statsDtoOut.getUri().substring(8)),
                            Integer.parseInt(String.valueOf(statsDtoOut.getHits())));
                }
            } catch (Exception e) {
                throw new GetStatisticException("Получить статистику не удалось. Необходимо исправить метод.");
            }
        }
        for (EventShortDto eventShortDto : shortEventsDto) {
            if (statistic.get(eventShortDto.getId()) != null) {
                eventShortDto.setViews(statistic.get(eventShortDto.getId()));
            }
        }
        return shortEventsDto;
    }

    public Event getEvent(int eventId) {
        return eventRepository.getReferenceById(eventId);
    }
}
