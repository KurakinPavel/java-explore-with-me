package ru.practicum.statsserverservice;

import ru.practicum.statsserverdto.dto.HitDto;
import ru.practicum.statsserverdto.dto.MomentFormatter;
import ru.practicum.statsserverservice.model.Hit;

import java.time.LocalDateTime;

public class HitMapper {

    public static HitDto toHitDto(Hit hit) {
        return new HitDto(
                hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getMoment().format(MomentFormatter.DATE_TIME_FORMAT)
        );
    }

    public static Hit toHit(HitDto hitDto) {
        return new Hit(
                hitDto.getId() != null ? hitDto.getId() : 0,
                hitDto.getApp() != null ? hitDto.getApp() : "",
                hitDto.getUri() != null ? hitDto.getUri() : "",
                hitDto.getIp() != null ? hitDto.getIp() : "",
                hitDto.getTimestamp() != null ? LocalDateTime.parse(hitDto.getTimestamp(), MomentFormatter.DATE_TIME_FORMAT) : null
        );
    }
}
