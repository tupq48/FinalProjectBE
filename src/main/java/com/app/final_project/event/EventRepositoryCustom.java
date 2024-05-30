package com.app.final_project.event;

import com.app.final_project.event.dto.EventDto;
import com.app.final_project.event.dto.EventRegistrationResponse;

import java.util.List;

public interface EventRepositoryCustom {
    public List<EventRegistrationResponse> getListEventAttended(Integer userId);

}
