package ru.practicum.statsserverservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statsserverdto.dto.HitDto;
import ru.practicum.statsserverdto.dto.StatsDtoOut;
import ru.practicum.statsserverdto.dto.StartEndValidationException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class HitService {
    private final HitRepository hitRepository;

    @Transactional
    public HitDto add(HitDto hitDto) {
        log.info("Сохранена история обращения к app {}", hitDto.getApp());
        return HitMapper.toHitDto(hitRepository.save(HitMapper.toHit(hitDto)));
    }

    @Transactional(readOnly = true)
    public List<StatsDtoOut> getHitStats(LocalDateTime startTime, LocalDateTime endTime, String[] uris, Boolean unique) {
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new StartEndValidationException("В запросе указан некорректный временной интервал");
        }
        if (uris != null && uris.length > 0) {
            if (unique) {
                return hitRepository.findUniqueIpHitsWithUris(startTime, endTime, List.of(uris));
            } else {
                return hitRepository.findHitsWithUris(startTime, endTime, List.of(uris));
            }
        } else {
            if (unique) {
                return hitRepository.findUniqueIpHits(startTime, endTime);
            } else {
                return hitRepository.findHits(startTime, endTime);
            }
        }
    }
}
