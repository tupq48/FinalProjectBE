package com.app.final_project.event;

import com.app.final_project.event.dto.EventDto;

import java.util.List;

public interface EventRepositoryCustom {
    public List<EventDto> getListEventAttended(Integer userId);

}
