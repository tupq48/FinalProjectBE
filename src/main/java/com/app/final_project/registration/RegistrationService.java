package com.app.final_project.registration;

import com.app.final_project.event.Event;
import com.app.final_project.event.EventService;
import com.app.final_project.notification.Notification;
import com.app.final_project.notification.NotificationService;
import com.app.final_project.registration.Interface.IRegistrationService;
import com.app.final_project.user.User;
import com.app.final_project.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RegistrationService implements IRegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public Boolean registerEvent(Integer eventId) {
        var eventOpt = eventService.getEventByIdWithLock(eventId);
        Event event = eventOpt.get();
        int maxAttendees = event.getMaxAttenders();
        int currentAttendees = registrationRepository.countByEventIdAndStatus(eventId, RegistrationStatus.registered);
        System.out.println("maxAttendees: " + maxAttendees + ", currentAttendees: " + currentAttendees );
        if (currentAttendees >= maxAttendees) {
            return false;
        }
        Integer userId = getCurrentUserId();
        if (userId == null) {
            return false;
        }

        if (eventOpt.isEmpty() || eventOpt.get().getStartTime().isBefore(LocalDateTime.now()))
        {
            return false;
        }

        Optional<Registration> existingRegistration = registrationRepository.findByEventIdAndUserId(eventId, userId);
        if (existingRegistration.isPresent()) {
            Registration registration = existingRegistration.get();
            registration.status = RegistrationStatus.registered;
            registration.registrationTime = LocalDateTime.now();
            registrationRepository.save(registration);
            return true;
        }

        Registration newRegistration = new Registration();
        newRegistration.setEventId(eventId);
        newRegistration.setUserId(userId);
        newRegistration.setRegistrationTime(LocalDateTime.now());
        newRegistration.setStatus(RegistrationStatus.registered);

        registrationRepository.save(newRegistration);
        return true;
    }

    @Override
    public Boolean cancelRegistration(Integer eventId) {
        Integer userId = getCurrentUserId();
        if (userId == null) {
            return false;
        }
        var eventOpt = eventService.getEventById(eventId);
        if (eventOpt.isEmpty() || eventOpt.get().getStartTime().isBefore(LocalDateTime.now()))
        {
            return false;
        }

        Optional<Registration> existingRegistration = registrationRepository.findByEventIdAndUserId(eventId, userId);
        if (existingRegistration.isEmpty()) {
            return false;
        }

        Registration registration = existingRegistration.get();
        registration.setRegistrationTime(LocalDateTime.now());
        registration.setStatus(RegistrationStatus.cancelled);
        registrationRepository.save(registration);
        return true;
    }

    @Override
    public boolean isUserRegistered(Integer eventId) {
        Integer userId = getCurrentUserId();
        if (userId == null) {
            return false;
        }
        var existingRegistration = registrationRepository.findByEventIdAndUserId(eventId, userId);
        return existingRegistration.map(registration -> registration.status.equals(RegistrationStatus.registered)).orElse(false);
    }
    @Override
    @Transactional
    public boolean registrationService(Integer userId, Integer eventId) {
        registrationRepository.deleteByEventIdAndUserId(eventId,userId);
        return true;
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

    @Override
    public Registration findByUserIdAndEventId(Integer userId, Integer eventId) {
        var resultOpt = registrationRepository.findByEventIdAndUserIdAndStatus(eventId, userId, RegistrationStatus.registered);
        return resultOpt.orElse(null);
    }
    @Override
    @Transactional
    public boolean updateStatusRegistrants(Integer eventId, Integer userId, Integer updateBy) {
        Boolean isSuccess = registrationRepository.updateStatusRegistrants(eventId, userId, updateBy);
        Notification notification = Notification.builder()
                .userId(userId)
                .eventId(eventId)
                .isUnRead(true)
                .createdAt(LocalDateTime.now())
                .build();

        if (isSuccess && updateBy == 0) {
            // thực hiện tạo thông báo đến user
            notification.setTitle("Admin");
            notification.setDescription("has approved your evidence.");
            notification.setAvatar("https://i.ibb.co/LJ4w8QN/accept.jpg");
            notificationService.save(notification);
        }

        if (isSuccess && updateBy == 1) {
            // thực hiện tạo thông báo đến user
            notification.setTitle("Admin");
            notification.setDescription("has rejected your evidence.");
            notification.setAvatar("https://i.ibb.co/19TddCK/reject.png");
            notificationService.save(notification);
        }
        return isSuccess;
    }
    @Override
    public boolean updateStatusRegistrantsPredicted(Integer eventId) {
        return registrationRepository.updateStatusRegistrantsPredicted(eventId);
    }

    public void save(Registration registration) {
        registrationRepository.save(registration);
    }
}
