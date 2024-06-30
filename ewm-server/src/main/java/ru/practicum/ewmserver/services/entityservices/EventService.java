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
import ru.practicum.ewmserver.searchparams.SearchParametersAdmin;
import ru.practicum.ewmserver.searchparams.SearchParametersUsersPublic;
import ru.practicum.statserverclient.client.StatsServerClient;
import ru.practicum.statsserverdto.dto.HitDto;
import ru.practicum.statsserverdto.dto.StartEndValidationException;
import ru.practicum.statsserverdto.dto.StatsDtoOut;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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
        List<EventFullDto> fullEventsDtoNoViews = events
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
        return addStatsToEventFullDtoInformation(fullEventsDtoNoViews).stream()
                .map(EventMapper::fromFullToShortEventDTO)
                .collect(Collectors.toList());
    }

    private List<EventFullDto> addStatsToEventFullDtoInformation(List<EventFullDto> eventsFullDto) {
        List<String> uris = new ArrayList<>();
        for (EventFullDto event : eventsFullDto) {
            uris.add("/events/" + event.getId());
        }
        Map<Integer, Integer> statistic = getHitsStatistic(uris);
        for (EventFullDto eventFullDto : eventsFullDto) {
            if (statistic.get(eventFullDto.getId()) != null) {
                eventFullDto.setViews(statistic.get(eventFullDto.getId()));
            }
        }
        return eventsFullDto;
    }

    private Map<Integer, Integer> getHitsStatistic(List<String> uris) {
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
                });
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
        return statistic;
    }

    public List<EventShortDto> getEventsWithFiltering(SearchParametersUsersPublic searchParametersUsersPublic,
                                                      PresentationParameters presentationParameters,
                                                      HttpServletRequest servletRequest) {
        statsServerClient.postHit(new HitDto(0,
                "emw-main-service",
                "/events",
                servletRequest.getRemoteAddr(),
                LocalDateTime.now().format(MomentFormatter.DATE_TIME_FORMAT)));
        Pageable pageable = PageRequest.of(presentationParameters.getFrom() / presentationParameters.getSize(),
                                            presentationParameters.getSize());
        String text = null;
        if (!searchParametersUsersPublic.getText().isBlank()) {
            text = searchParametersUsersPublic.getText().toLowerCase();
        }
        List<Integer> categories = null;
        if (!searchParametersUsersPublic.getCategories().isEmpty()) {
            categories = searchParametersUsersPublic.getCategories();
        }
        Boolean paid = null;
        if (searchParametersUsersPublic.getPaid() != null) {
            paid = searchParametersUsersPublic.getPaid();
        }
        LocalDateTime rangeStart;
        LocalDateTime rangeEnd;
        if (searchParametersUsersPublic.getRangeStart() != null && searchParametersUsersPublic.getRangeEnd() != null) {
            rangeStart = searchParametersUsersPublic.getRangeStart();
            rangeEnd = searchParametersUsersPublic.getRangeEnd();
            if (rangeStart.isAfter(rangeEnd)) {
                throw new StartEndValidationException("Переданы некорректные даты начала и окончания диапазона поиска");
            }
        } else if (searchParametersUsersPublic.getRangeStart() != null) {
            rangeStart = searchParametersUsersPublic.getRangeStart();
            rangeEnd = LocalDateTime.now().plusYears(1000);
        } else {
            rangeStart = LocalDateTime.now().minusYears(1000);
            rangeEnd = searchParametersUsersPublic.getRangeEnd();
        }
        Page<Event> events;
        events = eventRepository.findByParametersForPublic(EventState.PUBLISHED, text, categories, paid, rangeStart,
                rangeEnd, pageable);
        List<EventFullDto> fullEventsDtoNoViews = events
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
        List<EventFullDto> fullEventsDtoWithViews = addStatsToEventFullDtoInformation(fullEventsDtoNoViews);
        List<Integer> eventIds = new ArrayList<>();
        for (EventFullDto eventFullDto : fullEventsDtoWithViews) {
            eventIds.add(eventFullDto.getId());
        }
        Map<Integer, Integer> eventsConfirmedRequests = participationRequestService.countEventsConfirmedRequests(eventIds);
        for (EventFullDto eventFullDto : fullEventsDtoWithViews) {
            eventFullDto.setConfirmedRequests(eventsConfirmedRequests.get(eventFullDto.getId()) == null ? 0 :
                    eventsConfirmedRequests.get(eventFullDto.getId()));
        }
        Map<Integer, Integer> eventsParticipationLimit = new HashMap<>();
        for (Event event : events) {
            eventsParticipationLimit.put(event.getId(), event.getParticipantLimit());
        }
        List<EventFullDto> filteredByParticipationLimit = new ArrayList<>();
        for (EventFullDto eventFullDto : fullEventsDtoWithViews) {
            if (eventsParticipationLimit.get(eventFullDto.getId()) == 0 || eventFullDto.getConfirmedRequests()
                    < eventsParticipationLimit.get(eventFullDto.getId())) {
                filteredByParticipationLimit.add(eventFullDto);
            }
        }
        if (presentationParameters.getSort() == SortType.VIEWS) {
            filteredByParticipationLimit = filteredByParticipationLimit.stream()
                    .sorted(Comparator.comparing(EventFullDto::getViews).reversed())
                    .collect(Collectors.toList());
        }
        return filteredByParticipationLimit.stream()
                .map(EventMapper::fromFullToShortEventDTO)
                .collect(Collectors.toList());
    }

    public EventFullDto getEventForPublic(int id, HttpServletRequest servletRequest) {
        statsServerClient.postHit(new HitDto(0,
                "emw-main-service",
                "/events/" + id,
                servletRequest.getRemoteAddr(),
                LocalDateTime.now().format(MomentFormatter.DATE_TIME_FORMAT)));
        Event event = getEvent(id);
        if (event.getState() != EventState.PUBLISHED) {
            throw new NoSuchElementException("Событие с id " + id + " не опубликовано");
        }
        List<String> uris = new ArrayList<>();
        uris.add("/events/" + event.getId());
        Map<Integer, Integer> statistic = getHitsStatistic(uris);
        List<Integer> eventIds = new ArrayList<>();
        eventIds.add(event.getId());
        Map<Integer, Integer> eventsConfirmedRequests = participationRequestService.countEventsConfirmedRequests(eventIds);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        if (statistic.get(event.getId()) != null) {
            eventFullDto.setViews(statistic.get(event.getId()));
        }
        if (eventsConfirmedRequests.get(event.getId()) != null) {
            eventFullDto.setConfirmedRequests(eventsConfirmedRequests.get(event.getId()));
        }
        return eventFullDto;
    }

    public List<EventFullDto> getEventsWithFilteringForAdmin(SearchParametersAdmin searchParametersAdmin,
                                                             PresentationParameters presentationParameters) {
        Pageable pageable = PageRequest.of(presentationParameters.getFrom() / presentationParameters.getSize(),
                presentationParameters.getSize());
        List<Integer> users = null;
        if (!searchParametersAdmin.getUsers().isEmpty()) {
            users = searchParametersAdmin.getUsers();
        }
        List<EventState> states = null;
        if (!searchParametersAdmin.getStates().isEmpty()) {
            states = searchParametersAdmin.getStates();
        }
        List<Integer> categories = null;
        if (!searchParametersAdmin.getCategories().isEmpty()) {
            categories = searchParametersAdmin.getCategories();
        }
        LocalDateTime rangeStart;
        LocalDateTime rangeEnd;
        if (searchParametersAdmin.getRangeStart() != null && searchParametersAdmin.getRangeEnd() != null) {
            rangeStart = searchParametersAdmin.getRangeStart();
            rangeEnd = searchParametersAdmin.getRangeEnd();
            if (rangeStart.isAfter(rangeEnd)) {
                throw new StartEndValidationException("Переданы некорректные даты начала и окончания диапазона поиска");
            }
        } else if (searchParametersAdmin.getRangeStart() != null) {
            rangeStart = searchParametersAdmin.getRangeStart();
            rangeEnd = LocalDateTime.now().plusYears(1000);
        } else {
            rangeStart = LocalDateTime.now().minusYears(1000);
            rangeEnd = searchParametersAdmin.getRangeEnd();
        }
        Page<Event> events;
        events = eventRepository.findByParametersForAdmin(users, states, categories, rangeStart, rangeEnd, pageable);
        List<EventFullDto> fullEventsDtoNoViews = events
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
        List<EventFullDto> fullEventsDtoWithViews = addStatsToEventFullDtoInformation(fullEventsDtoNoViews);
        List<Integer> eventIds = new ArrayList<>();
        for (EventFullDto eventFullDto : fullEventsDtoWithViews) {
            eventIds.add(eventFullDto.getId());
        }
        Map<Integer, Integer> eventsConfirmedRequests = participationRequestService.countEventsConfirmedRequests(eventIds);
        for (EventFullDto eventFullDto : fullEventsDtoWithViews) {
            eventFullDto.setConfirmedRequests(eventsConfirmedRequests.get(eventFullDto.getId()) == null ? 0 :
                    eventsConfirmedRequests.get(eventFullDto.getId()));
        }
        return fullEventsDtoWithViews;
    }

    public Event getEvent(int eventId) {
        return eventRepository.getReferenceById(eventId);
    }
}
