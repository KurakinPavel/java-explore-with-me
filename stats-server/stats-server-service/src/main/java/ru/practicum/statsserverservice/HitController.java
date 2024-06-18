package ru.practicum.statsserverservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.statsserverdto.dto.HitDto;
import ru.practicum.statsserverdto.dto.StatsDtoOut;

import javax.validation.Valid;
import java.util.List;

@Controller
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
    public List<StatsDtoOut> getHitStats(@RequestParam String start,
                                          @RequestParam String end,
                                          @RequestParam (required = false) String[] uris,
                                          @RequestParam (defaultValue = "false") Boolean unique) {
        return hitService.getHitStats(start, end, uris, unique);
    }
}
