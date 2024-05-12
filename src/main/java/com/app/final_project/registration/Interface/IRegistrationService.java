package com.app.final_project.registration.Interface;

public interface IRegistrationService {
    public Boolean registerEvent(Integer eventId);

    Boolean cancelRegistration(Integer eventId);

    boolean isUserRegistered(Integer eventId);
}
