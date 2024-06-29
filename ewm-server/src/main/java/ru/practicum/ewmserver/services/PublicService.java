package ru.practicum.ewmserver.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmserver.dto.EventShortDto;
import ru.practicum.ewmserver.searchparams.PresentationParameters;
import ru.practicum.ewmserver.searchparams.SearchParameters;
import ru.practicum.ewmserver.services.entityservices.CategoryService;
import ru.practicum.ewmserver.services.entityservices.EventService;
import ru.practicum.ewmserver.services.entityservices.ParticipationRequestService;
import ru.practicum.ewmserver.services.entityservices.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PublicService {
    private final EventService eventService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ParticipationRequestService participationRequestService;

    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsWithFiltering(SearchParameters searchParameters,
                                                      PresentationParameters presentationParameters,
                                                      HttpServletRequest servletRequest) {
        return eventService.getEventsWithFiltering(searchParameters, presentationParameters, servletRequest);
    }

}
