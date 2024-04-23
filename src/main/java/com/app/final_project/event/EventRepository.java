package com.app.final_project.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query(value = "CALL GetEventsByPage(:limit, :offset)", nativeQuery = true)
    List<Object[]> findEventsByPage(@Param("limit") int limit, @Param("offset") int offset);
}
