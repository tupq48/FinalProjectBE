package com.app.final_project.event;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>, EventRepositoryCustom {

    @Query(value = "CALL GetEventsByPage(:limit, :offset)", nativeQuery = true)
    List<Object[]> findEventsByPage(@Param("limit") int limit, @Param("offset") int offset);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Event e WHERE e.eventId = :eventId")
    Optional<Event> findByIdWithLock(Integer eventId);
}
