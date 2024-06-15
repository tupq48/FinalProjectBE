package com.app.final_project.notification;

import com.app.final_project.user.User;
import com.app.final_project.user.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    UserService userService;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Notification> getAllNotification() {
        Integer userId = getCurrentUserId();
        return notificationRepository.findAllByUserId(userId).stream()
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .toList();
    }

    public void addNotificationForAllUser(Integer eventId, String imageUrl) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("AddNotificationForAllUser")
                .registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter(2, String.class, ParameterMode.IN);

        query.setParameter(1, eventId);
        query.setParameter(2, imageUrl);

        query.execute();
    }

    public Boolean setReadNotification(Integer id) {
        Integer userId = getCurrentUserId();
        var notificationOpt = notificationRepository.findById(id);
        if (notificationOpt.isEmpty())
            return false;
        var notification = notificationOpt.get();
        if (!Objects.equals(notification.getUserId(), userId))
            return false;
        notification.setIsUnRead(false);
        notificationRepository.save(notification);
        return true;
    }

    public Boolean setReadAllNotification() {
        Integer userId = getCurrentUserId();
        var notifications = notificationRepository.findAllByUserId(userId);
        for (Notification notification : notifications) {
            notification.setIsUnRead(false);
        }
        notificationRepository.saveAll(notifications);
        return true;
    }

    public void removeNotificationByEventId(Integer eventId) {
        notificationRepository.deleteAllByEventId(eventId);
    }

    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> userOpt = userService.getUserByUserName(userName);
        if (userOpt.isEmpty()) {
            return null;
        }
        return userOpt.get().getUser_id();
    }
}
