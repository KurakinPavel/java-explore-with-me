package ru.practicum.statsserverservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsserverdto.dto.HitDto;
import ru.practicum.statsserverdto.dto.StatsDtoOut;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class HitController {
    private final HitService hitService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto add(@RequestBody @Valid HitDto hitDto) {
        return hitService.add(hitDto);
    }

    @GetMapping("/stats")
    public List<StatsDtoOut> getHitStats(@RequestParam @NotBlank @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                         @RequestParam @NotBlank @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                         @RequestParam (required = false) String[] uris,
                                         @RequestParam (defaultValue = "false") Boolean unique) {
        log.info("Поступил запрос с параметрами: start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        return hitService.getHitStats(start, end, uris, unique);
    }
}
