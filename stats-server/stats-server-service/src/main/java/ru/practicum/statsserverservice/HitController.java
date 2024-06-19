package ru.practicum.statsserverservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsserverdto.dto.HitDto;
import ru.practicum.statsserverdto.dto.StatsDtoOut;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
    public List<StatsDtoOut> getHitStats(@RequestParam @NotBlank @Size(min = 19) String start,
                                          @RequestParam @NotBlank @Size(min = 19) String end,
                                          @RequestParam (required = false) String[] uris,
                                          @RequestParam (defaultValue = "false") Boolean unique) {
        return hitService.getHitStats(start, end, uris, unique);
    }
}
