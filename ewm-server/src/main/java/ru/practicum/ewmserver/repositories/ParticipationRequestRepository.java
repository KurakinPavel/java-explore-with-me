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

    ParticipationRequest findOneByEvent_IdAndRequester_Id(int eventId, int requesterId);

    List<ParticipationRequest> findAllByIdIn(List<Integer> requestIds);

    List<ParticipationRequest> findAllByRequester_Id(int requesterId);

    List<ParticipationRequest> findAllByEvent_Id(int eventId);
}
