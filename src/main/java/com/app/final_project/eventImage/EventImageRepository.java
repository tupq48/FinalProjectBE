package com.app.final_project.eventImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventImageRepository extends JpaRepository<EventImage, Integer> {

    List<EventImage> findAllByEventIdIn(List<Integer> eventIds);

    List<EventImage> findAllByEventId(Integer eventId);

    void deleteAllByEventId(Integer eventId);
}
