package com.app.final_project.event;

import com.app.final_project.event.dto.EventDto;
import com.app.final_project.event.dto.EventRegistrationResponse;
import com.app.final_project.registration.dto.AttendanceImage;

import java.util.List;

public interface EventRepositoryCustom {
    public List<EventRegistrationResponse> getListEventAttended(Integer userId);
    public List<AttendanceImage> getListImagesUser(Integer userId, Integer eventId);
    List<Object[]> getEventByStatus(Integer pageSize, Integer page, Integer filterBy);

}
