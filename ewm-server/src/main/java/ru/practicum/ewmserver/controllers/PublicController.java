package ru.practicum.ewmserver.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmserver.dto.CategoryDto;
import ru.practicum.ewmserver.dto.CompilationDto;
import ru.practicum.ewmserver.dto.EventFullDto;
import ru.practicum.ewmserver.dto.EventShortDto;
import ru.practicum.ewmserver.enums.SortType;
import ru.practicum.ewmserver.searchparams.PresentationParameters;
import ru.practicum.ewmserver.searchparams.SearchParametersUsersPublic;
import ru.practicum.ewmserver.services.PublicService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
@Slf4j
public class PublicController {
    private final PublicService publicService;

    @GetMapping("/events")
    public List<EventShortDto> getEventsWithFiltering(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Integer> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false) SortType sort,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size, HttpServletRequest servletRequest) {

        SearchParametersUsersPublic searchParametersUsersPublic = new SearchParametersUsersPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable);
        PresentationParameters presentationParameters = new PresentationParameters(sort, from, size);
        return publicService.getEventsWithFiltering(searchParametersUsersPublic, presentationParameters, servletRequest);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventForPublic(@PathVariable @Positive Integer id,
                                          HttpServletRequest servletRequest) {

        return publicService.getEventForPublic(id, servletRequest);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "10") @Positive Integer size) {
        return publicService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable @Positive Integer catId) {
        return publicService.getCategoryById(catId);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable @Positive Integer compId) {
        return publicService.getCompilationById(compId);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(
            @RequestParam(required = false) @NotNull Boolean pinned,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        return publicService.getCompilations(pinned, from, size);
    }
}
