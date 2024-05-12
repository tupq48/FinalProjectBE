package com.app.final_project.registration;

import com.app.final_project.event.EventService;
import com.app.final_project.registration.Interface.IRegistrationService;
import com.app.final_project.user.User;
import com.app.final_project.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    @Override
    public Boolean registerEvent(Integer eventId) {
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
