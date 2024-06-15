package com.app.final_project.notification;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findAllByUserId(Integer userId);

    List<Notification> findAllByEventId(Integer eventId);

    @Transactional
    void deleteAllByEventId(Integer eventId);
}
