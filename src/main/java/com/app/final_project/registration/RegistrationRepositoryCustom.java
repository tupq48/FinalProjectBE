package com.app.final_project.registration;

public interface RegistrationRepositoryCustom {
    boolean updateStatusRegistrants(Integer eventId, Integer userId, Integer updateBy);
    boolean updateStatusRegistrantsPredicted(Integer eventId);

}
