package ru.practicum.ewmserver.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmserver.dto.CategoryDto;
import ru.practicum.ewmserver.dto.EventFullDto;
import ru.practicum.ewmserver.dto.UpdateEventRequest;
import ru.practicum.ewmserver.dto.UserDto;
import ru.practicum.ewmserver.services.AdminService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin")
@AllArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@RequestBody @Valid UserDto userDto) {
        log.info("Request from controller for post new User");
        return adminService.save(userDto);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto save(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Request from controller for post new Category");
        return adminService.save(categoryDto);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto update(@PathVariable Integer catId,
                              @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Request from controller for update Category id = {}", catId);
        return adminService.update(catId, categoryDto);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto update(@PathVariable Integer eventId,
                               @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        log.info("Request from admin controller for update Event id = {}", eventId);
        return adminService.update(eventId, updateEventRequest);
    }
}
