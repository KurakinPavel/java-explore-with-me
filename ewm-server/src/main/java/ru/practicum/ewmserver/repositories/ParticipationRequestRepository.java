package ru.practicum.ewmserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmserver.dto.ConfirmedRequestsStats;
import ru.practicum.ewmserver.enums.ParticipationRequestStatus;
import ru.practicum.ewmserver.model.ParticipationRequest;

import java.util.List;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer> {

    @Query( "SELECT new ru.practicum.ewmserver.dto.ConfirmedRequestsStats(pr.event.id, COUNT(pr.id)) " +
            "FROM ParticipationRequest AS pr " +
            "WHERE pr.status = :status AND pr.event.id IN :eventIds " +
            "GROUP BY pr.event.id")
    List<ConfirmedRequestsStats> findStatsForRequests(@Param("status") ParticipationRequestStatus status,
            @Param("eventIds") List<Integer> eventIds);
}
