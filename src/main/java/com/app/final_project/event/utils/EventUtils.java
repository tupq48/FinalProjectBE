package com.app.final_project.event.utils;

import com.app.final_project.event.dto.EventDto;
import com.app.final_project.event.Event;
import com.app.final_project.event.dto.EventRequest;

import java.time.LocalDateTime;
import java.util.List;

public class EventUtils {
    public static EventDto convertEventToEventDto(Event event, List<String> eventUrls) {
        return EventDto.builder()
                .eventId(event.getEventId())
                .eventName(event.getEventName())
                .point(event.getPoint())
                .description(event.getDescription())
                .location(event.getLocation())
                .maxAttenders(event.getMaxAttenders())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .imageUrls(eventUrls)
                .build();
    }

    public static Event convertEventRequestToEvent(EventRequest eventRequest) {
        return Event.builder()
                .point(eventRequest.getPoint())
                .maxAttenders(eventRequest.getMaxAttenders())
                .eventName(eventRequest.getEventName())
                .location(eventRequest.getLocation())
                .description(eventRequest.getDescription())
                .startTime(eventRequest.getStartTime())
                .endTime(eventRequest.getEndTime())
                .build();
    }
}
