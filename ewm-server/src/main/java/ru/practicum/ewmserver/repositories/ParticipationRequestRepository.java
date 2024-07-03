package ru.practicum.ewmserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmserver.model.ParticipationRequest;

import java.util.List;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer> {

    ParticipationRequest findOneByEvent_IdAndRequester_Id(int eventId, int requesterId);

    List<ParticipationRequest> findAllByIdIn(List<Integer> requestIds);

    List<ParticipationRequest> findAllByRequester_Id(int requesterId);

    List<ParticipationRequest> findAllByEvent_Id(int eventId);
}
