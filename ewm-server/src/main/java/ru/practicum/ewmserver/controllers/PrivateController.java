package ru.practicum.ewmserver.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmserver.dto.EventFullDto;
import ru.practicum.ewmserver.dto.NewEventDto;
import ru.practicum.ewmserver.services.PrivateService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class PrivateController {
    private final PrivateService privateService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto save(@PathVariable Integer userId,
                             @RequestBody @Valid NewEventDto newEventDto) {
        return privateService.save(userId, newEventDto);
    }
}
