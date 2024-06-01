package com.app.final_project.registration.Interface;

import com.app.final_project.registration.Registration;

public interface IRegistrationService {
    public Boolean registerEvent(Integer eventId);

    Boolean cancelRegistration(Integer eventId);

    boolean isUserRegistered(Integer eventId);
    boolean registrationService(Integer userId, Integer eventId);

    Registration findByUserIdAndEventId(Integer userId, Integer eventId);

}
