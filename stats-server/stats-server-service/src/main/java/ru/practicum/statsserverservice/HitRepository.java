package ru.practicum.statsserverservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.statsserverdto.dto.StatsDtoOut;
import ru.practicum.statsserverservice.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Integer> {

    @Query("SELECT new ru.practicum.statsserverdto.dto.StatsDtoOut(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit AS h " +
            "WHERE h.moment BETWEEN :start AND :end AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<StatsDtoOut> findUniqueIpHitsWithUris(@Param("start") LocalDateTime startTime,
                                               @Param("end") LocalDateTime endTime,
                                               @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.statsserverdto.dto.StatsDtoOut(h.app, h.uri, COUNT(h.ip)) " +
            "FROM Hit AS h " +
            "WHERE h.moment BETWEEN :start AND :end AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<StatsDtoOut> findHitsWithUris(@Param("start") LocalDateTime startTime,
                                       @Param("end") LocalDateTime endTime,
                                       @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.statsserverdto.dto.StatsDtoOut(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit AS h " +
            "WHERE h.moment BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<StatsDtoOut> findUniqueIpHits(@Param("start") LocalDateTime startTime,
                                       @Param("end") LocalDateTime endTime);

    @Query("SELECT new ru.practicum.statsserverdto.dto.StatsDtoOut(h.app, h.uri, COUNT(h.ip)) " +
            "FROM Hit AS h " +
            "WHERE h.moment BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<StatsDtoOut> findHits(@Param("start") LocalDateTime startTime,
                               @Param("end") LocalDateTime endTime);
}
