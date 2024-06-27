package ru.practicum.ewmserver.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmserver.dto.EventShortDto;
import ru.practicum.ewmserver.enums.SortType;
import ru.practicum.ewmserver.services.PublicService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
@Slf4j
public class PublicController {
    private final PublicService publicService;

//    @GetMapping("/events")
//    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
//                                         @RequestParam(required = false) List<Integer> categories,
//                                         @RequestParam(required = false) Boolean paid,
//                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
//                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
//                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
//                                         @RequestParam SortType sort,
//                                         @RequestParam(defaultValue = "0") Integer from,
//                                         @RequestParam(defaultValue = "10") Integer size) {
//
//    }

}
