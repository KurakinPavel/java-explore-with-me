package ru.practicum.ewmserver.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmserver.enums.EventState;
import ru.practicum.ewmserver.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    Page<Event> findAllByInitiator_Id(int userId, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.state = :state) " +
                "AND ((LOWER(e.annotation) LIKE CONCAT('%', :text, '%') " +
                   "OR LOWER(e.description) LIKE CONCAT('%', :text, '%')) OR :text IS NULL) " +
                "AND (e.category.id IN :categories OR :categories IS NULL) " +
                "AND (CAST(e.paid AS boolean) = :paid OR :paid IS NULL) " +
                "AND (e.eventDate BETWEEN :start AND :end) " +
            "ORDER BY e.eventDate")
    Page<Event> findByParametersForPublic(@Param("state") EventState state,
                                          @Param("text") String text,
                                          @Param("categories") List<Integer> categories,
                                          @Param("paid") Boolean paid,
                                          @Param("start") LocalDateTime rangeStart,
                                          @Param("end") LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.initiator.id IN :users OR :users IS NULL) " +
                "AND (e.state IN :states OR :states IS NULL) " +
                "AND (e.category.id IN :categories OR :categories IS NULL) " +
                "AND (e.eventDate BETWEEN :start AND :end)")
    Page<Event> findByParametersForAdmin(@Param("users") List<Integer> users,
                                         @Param("states") List<EventState> states,
                                         @Param("categories") List<Integer> categories,
                                         @Param("start") LocalDateTime rangeStart,
                                         @Param("end") LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.rating <> 0 " +
            "ORDER BY e.rating DESC")
    Page<Event> findAllWhereRatingNotEqualToZeroSortByRatingDesc(Pageable pageable);
}
