package com.app.final_project.event;

import com.app.final_project.event.dto.EventRequest;
import com.app.final_project.eventImage.EventImageService;
import com.app.final_project.event.dto.EventDto;
import com.app.final_project.event.dto.EventResponse;
import com.app.final_project.event.exception.EventNotFoundException;
import com.app.final_project.event.utils.EventUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventImageService eventImageService;

    public EventDto findEventById(Integer eventId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isEmpty() || eventOpt.get().getIsDeleted())
            throw new EventNotFoundException(eventId);
        List<String> eventUrls = eventImageService.findAllByEventId(eventId);
        return EventUtils.convertEventToEventDto(eventOpt.get(), eventUrls);
    }

    public Optional<Event> getEventById(Integer eventId) {
        return eventRepository.findById(eventId);
    }

    public EventResponse getEventByPage(int pageSize, int page) {
        EventResponse result = new EventResponse();

        int offset = (page - 1) * pageSize;
        var rows = eventRepository.findEventsByPage(pageSize, offset);
        List<EventDto> eventDtos = new ArrayList<>();
        rows.forEach(
            row -> {
                int eventId = (int) row[0];
                String eventName = (String) row[1];
                Timestamp startTimeTimestamp = (Timestamp) row[2];
                Timestamp endTimeTimestamp = (Timestamp) row[3];
                LocalDateTime startTime = startTimeTimestamp.toLocalDateTime();
                LocalDateTime endTime = endTimeTimestamp.toLocalDateTime();
                String location = (String) row[4];
                int point = (int) row[5];
                String description = (String) row[6];
                int maxAttenders = (int) row[7];
                boolean isDeleted = (boolean) row[8];
                List<String> imageURLs = Arrays.asList(((String)row[9]).split(","));
                long total = (long) row[10];
                eventDtos.add(EventDto.builder()
                        .eventId(eventId)
                        .startTime(startTime)
                        .endTime(endTime)
                        .eventName(eventName)
                        .location(location)
                        .point(point)
                        .description(description)
                        .maxAttenders(maxAttenders)
                        .imageUrls(imageURLs)
                        .build());
                result.setTotal(total);
            }
        );
        result.setEventDtos(eventDtos);
        return result;
    }

    @Transactional
    public Event createEvent(EventRequest eventRequest, List<MultipartFile> images) {
        Event event = EventUtils.convertEventRequestToEvent(eventRequest);
        Event savedEvent = eventRepository.save(event);
        eventImageService.saveListEventImage(savedEvent.getEventId(), images);
        return savedEvent;
    }

    @Transactional
    public Event updateEvent(EventRequest eventRequest, List<MultipartFile> images) {
        Event event = EventUtils.convertEventRequestToEvent(eventRequest);
        Event updatedEvent = eventRepository.save(event);
        if (images != null) {
            eventImageService.deleteAllByEventId(eventRequest.getEventId());
            eventImageService.saveListEventImage(updatedEvent.getEventId(), images);
        }
        return updatedEvent;
    }

    public boolean deleteEvent(Integer eventId) {
        var eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isEmpty())
            return false;
        Event event = eventOpt.get();
        event.setIsDeleted(true);
        eventRepository.save(event);
        return true;
    }
}
