package ru.practicum.ewmserver.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmserver.dto.CategoryDto;
import ru.practicum.ewmserver.dto.EventFullDto;
import ru.practicum.ewmserver.dto.UpdateEventRequest;
import ru.practicum.ewmserver.dto.UserDto;
import ru.practicum.ewmserver.model.Category;
import ru.practicum.ewmserver.searchparams.PresentationParameters;
import ru.practicum.ewmserver.searchparams.SearchParametersAdmin;
import ru.practicum.ewmserver.services.entityservices.CategoryService;
import ru.practicum.ewmserver.services.entityservices.EventService;
import ru.practicum.ewmserver.services.entityservices.UserService;

import javax.servlet.ServletRequest;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AdminService {
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;

    @Transactional
    public UserDto save(UserDto userDto) {
        return userService.save(userDto);
    }

    @Transactional
    public CategoryDto save(CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @Transactional
    public CategoryDto update(int catId, CategoryDto categoryDto) {
        return categoryService.update(catId, categoryDto);
    }

    @Transactional
    public EventFullDto update(int eventId, UpdateEventRequest updateEventRequest) {
        Category category = null;
        if (updateEventRequest.getCategory() != null) {
            category = categoryService.getCategory(updateEventRequest.getCategory());
        }
        return eventService.updateByAdmin(eventId, updateEventRequest, category);
    }

    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsWithFilteringForAdmin(SearchParametersAdmin searchParametersAdmin,
                                                             PresentationParameters presentationParameters) {
        return eventService.getEventsWithFilteringForAdmin(searchParametersAdmin, presentationParameters);
    }
}
