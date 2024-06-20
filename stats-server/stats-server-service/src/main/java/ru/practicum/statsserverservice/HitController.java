package ru.practicum.statsserverservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsserverdto.dto.HitDto;
import ru.practicum.statsserverdto.dto.StatsDtoOut;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class HitController {
    private final HitService hitService;

    @PostMapping("/hit")
    public HitDto add(@RequestBody @Valid HitDto hitDto) {
        return hitService.add(hitDto);
    }

    @GetMapping("/stats")
    public List<StatsDtoOut> getHitStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                         @RequestParam (required = false) String[] uris,
                                         @RequestParam (defaultValue = "false") Boolean unique) {
        return hitService.getHitStats(start, end, uris, unique);
    }
}
