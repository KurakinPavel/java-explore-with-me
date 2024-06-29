package ru.practicum.ewmserver.services.entityservices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.ewmserver.enums.SortType;
import ru.practicum.ewmserver.enums.UserStateAction;
import ru.practicum.ewmserver.exceptions.custom.GetStatisticException;
import ru.practicum.ewmserver.exceptions.custom.UserValidationException;
import ru.practicum.ewmserver.mappers.EventMapper;
import ru.practicum.ewmserver.model.Category;
import ru.practicum.ewmserver.model.Event;
import ru.practicum.ewmserver.model.User;
import ru.practicum.ewmserver.repositories.EventRepository;
import ru.practicum.ewmserver.searchparams.PresentationParameters;
import ru.practicum.ewmserver.searchparams.SearchParameters;
import ru.practicum.statserverclient.client.StatsServerClient;
import ru.practicum.statsserverdto.dto.HitDto;
import ru.practicum.statsserverdto.dto.StartEndValidationException;
import ru.practicum.statsserverdto.dto.StatsDtoOut;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
    private final ParticipationRequestService participationRequestService;
    private final ObjectMapper objectMapper;

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
        List<EventShortDto> shortEventsDtoNoViews = events
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
        return addStatsToEventShortDtoInformation(shortEventsDtoNoViews);
    }

    private List<EventShortDto> addStatsToEventShortDtoInformation(List<EventShortDto> eventsShortDto) {
        List<String> uris = new ArrayList<>();
        for (EventShortDto event : eventsShortDto) {
            uris.add("/events/" + event.getId());
        }
        String start = LocalDateTime.now().minusYears(100).format(MomentFormatter.DATE_TIME_FORMAT);
        String end = LocalDateTime.now().plusYears(100).format(MomentFormatter.DATE_TIME_FORMAT);
        log.info("Передаю: {}, {}", start, end);
        ResponseEntity<Object> response = statsServerClient.getHitsStatistics(start, end, uris, true);
        log.info("Статистику получил. Обрабатываю.");
        log.info(String.valueOf(response.getStatusCode().is2xxSuccessful()));
        Map<Integer, Integer> statistic = new HashMap<>();
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Статистику получил. Обрабатываю.");
            try {
                List<StatsDtoOut> stats = objectMapper.convertValue(response.getBody(), new TypeReference<>() {
                }); // List<StatsDtoOut>
                for (StatsDtoOut statsDtoOut : stats) {
                    statistic.put(Integer.parseInt(statsDtoOut.getUri().substring(8)),
                            Integer.parseInt(String.valueOf(statsDtoOut.getHits())));
                    log.info("Вставляю в мапу: {}, {}", Integer.parseInt(statsDtoOut.getUri().substring(8)),
                            Integer.parseInt(String.valueOf(statsDtoOut.getHits())));
                }
            } catch (Exception e) {
                throw new GetStatisticException("Получить статистику не удалось.");
            }
        }
        for (EventShortDto eventShortDto : eventsShortDto) {
            if (statistic.get(eventShortDto.getId()) != null) {
                eventShortDto.setViews(statistic.get(eventShortDto.getId()));
            }
        }
        return eventsShortDto;
    }

    public List<EventShortDto> getEventsWithFiltering(SearchParameters searchParameters,
                                                      PresentationParameters presentationParameters,
                                                      HttpServletRequest servletRequest) {
        statsServerClient.postHit(new HitDto(0, "emw-main-service", "/events", servletRequest.getRemoteAddr(), LocalDateTime.now().format(MomentFormatter.DATE_TIME_FORMAT)));
        Pageable pageable = PageRequest.of(presentationParameters.getFrom() / presentationParameters.getSize(), presentationParameters.getSize());
        String text = null;
        if (!searchParameters.getText().isBlank()) {
            text = searchParameters.getText().toLowerCase();
        }
        List<Integer> categories = null;
        if (!searchParameters.getCategories().isEmpty()) {
            categories = searchParameters.getCategories();
        }
        Boolean paid = null;
        if (searchParameters.getPaid() != null) {
            paid = searchParameters.getPaid();
        }
        LocalDateTime rangeStart;
        LocalDateTime rangeEnd;
        if (searchParameters.getRangeStart() != null && searchParameters.getRangeEnd() != null) {
            rangeStart = searchParameters.getRangeStart();
            rangeEnd = searchParameters.getRangeEnd();
            if (rangeStart.isAfter(rangeEnd)) {
                throw new StartEndValidationException("Переданы некорректные даты начала и окончания диапазона поиска");
            }
        } else if (searchParameters.getRangeStart() != null) {
            rangeStart = searchParameters.getRangeStart();
            rangeEnd = LocalDateTime.now().plusYears(1000);
        } else {
            rangeStart = LocalDateTime.now().minusYears(1000);
            rangeEnd = searchParameters.getRangeEnd();
        }
        Page<Event> events;
        events = eventRepository.findByParametersForPublic(EventState.PUBLISHED, text, categories, paid, rangeStart, rangeEnd, pageable);
        List<EventShortDto> shortEventsDtoNoViews = events
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
        List<EventShortDto> shortEventsDtoWithViews = addStatsToEventShortDtoInformation(shortEventsDtoNoViews);
        List<Integer> eventIds = new ArrayList<>();
        for (EventShortDto eventShortDto : shortEventsDtoWithViews) {
            eventIds.add(eventShortDto.getId());
        }
        Map<Integer, Integer> eventsConfirmedRequests = participationRequestService.countEventsConfirmedRequests(eventIds);
        for (EventShortDto eventShortDto : shortEventsDtoWithViews) {
            eventShortDto.setConfirmedRequests(eventsConfirmedRequests.get(eventShortDto.getId()) == null ? 0 : eventsConfirmedRequests.get(eventShortDto.getId()));
        }
        Map<Integer, Integer> eventsParticipationLimit = new HashMap<>();
        for (Event event : events) {
            eventsParticipationLimit.put(event.getId(), event.getParticipantLimit());
        }
        List<EventShortDto> filteredByParticipationLimit = new ArrayList<>();
        for (EventShortDto eventShortDto : shortEventsDtoWithViews) {
            if (eventsParticipationLimit.get(eventShortDto.getId()) == 0 || eventShortDto.getConfirmedRequests() < eventsParticipationLimit.get(eventShortDto.getId())) {
                filteredByParticipationLimit.add(eventShortDto);
            }
        }
        if (presentationParameters.getSort() == SortType.VIEWS) {
            filteredByParticipationLimit = filteredByParticipationLimit.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews).reversed())
                    .collect(Collectors.toList());
        }
        return filteredByParticipationLimit;
    }

    public Event getEvent(int eventId) {
        return eventRepository.getReferenceById(eventId);
    }
}
