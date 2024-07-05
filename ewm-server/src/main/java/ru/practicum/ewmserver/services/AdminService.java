package ru.practicum.ewmserver.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmserver.dto.category.CategoryDto;
import ru.practicum.ewmserver.dto.compilation.CompilationDto;
import ru.practicum.ewmserver.dto.event.EventFullDto;
import ru.practicum.ewmserver.dto.compilation.NewCompilationDto;
import ru.practicum.ewmserver.dto.event.UpdateEventRequest;
import ru.practicum.ewmserver.dto.user.UserDto;
import ru.practicum.ewmserver.mappers.CompilationMapper;
import ru.practicum.ewmserver.model.Category;
import ru.practicum.ewmserver.model.Compilation;
import ru.practicum.ewmserver.model.Event;
import ru.practicum.ewmserver.searchparams.PresentationParameters;
import ru.practicum.ewmserver.searchparams.SearchParametersAdmin;
import ru.practicum.ewmserver.services.entityservices.CategoryService;
import ru.practicum.ewmserver.services.entityservices.CompilationService;
import ru.practicum.ewmserver.services.entityservices.EventService;
import ru.practicum.ewmserver.services.entityservices.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class AdminService {
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;

    /** Категории */

    @Transactional
    public CategoryDto saveCategory(CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCategory(int catId) {
        categoryService.deleteCategory(catId);
    }

    @Transactional
    public CategoryDto updateCategory(int catId, CategoryDto categoryDto) {
        return categoryService.update(catId, categoryDto);
    }

    /** События */

    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsWithFilteringForAdmin(SearchParametersAdmin searchParametersAdmin,
                                                             PresentationParameters presentationParameters) {
        return eventService.getEventsWithFilteringForAdmin(searchParametersAdmin, presentationParameters);
    }

    @Transactional
    public EventFullDto updateEvent(int eventId, UpdateEventRequest updateEventRequest) {
        Category category = null;
        if (updateEventRequest.getCategory() != null) {
            category = categoryService.getCategory(updateEventRequest.getCategory());
        }
        return eventService.updateByAdmin(eventId, updateEventRequest, category);
    }

    /** Пользователи */

    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        return userService.getUsers(ids, from, size);
    }

    @Transactional
    public UserDto saveUser(UserDto userDto) {
        return userService.save(userDto);
    }

    @Transactional
    public void deleteUser(int userId) {
        userService.deleteUser(userId);
    }

    /** Подборки событий */

    @Transactional
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getTitle() == null || newCompilationDto.getTitle().isBlank()) {
            throw new IllegalArgumentException("При создании подборки отсутствие заголовка не допускается");
        }
        List<Integer> eventsIds = new ArrayList<>();
        if (newCompilationDto.getEvents() != null) {
            eventsIds = newCompilationDto.getEvents();
        }
        Set<Event> eventsForCompilation = new HashSet<>(eventService.getEventsByIds(eventsIds));
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, eventsForCompilation);
        return CompilationMapper.toCompilationDto(compilationService.saveCompilation(compilation));
    }

    @Transactional
    public void deleteCompilation(int compId) {
        compilationService.deleteCompilation(compId);
    }

    @Transactional
    public CompilationDto updateCompilation(int compId, NewCompilationDto newCompilationDto) {
        Compilation updatingCompilation = compilationService.getCompilation(compId);
        if (newCompilationDto.getTitle() != null) {
            updatingCompilation.setTitle(newCompilationDto.getTitle());
        }
        if (newCompilationDto.getPinned() != null) {
            updatingCompilation.setPinned(newCompilationDto.getPinned());
        }
        if (newCompilationDto.getEvents() != null) {
            Set<Event> eventsForCompilation = new HashSet<>(eventService.getEventsByIds(newCompilationDto.getEvents()));
            updatingCompilation.setEvents(eventsForCompilation);
        }
        return CompilationMapper.toCompilationDto(compilationService.saveCompilation(updatingCompilation));
    }
}
