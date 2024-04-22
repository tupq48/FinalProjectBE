package com.app.final_project.event;

import com.app.final_project.event.dto.EventRequest;
import com.app.final_project.eventImage.EventImage;
import com.app.final_project.eventImage.EventImageService;
import com.app.final_project.event.dto.EventDto;
import com.app.final_project.event.dto.EventResponse;
import com.app.final_project.event.exception.EventNotFoundException;
import com.app.final_project.event.utils.EventUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventImageService eventImageService;

    public EventDto findEventById(Integer eventId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isEmpty())
            throw new EventNotFoundException(eventId);
        List<String> eventUrls = eventImageService.findAllByEventId(eventId);
        return EventUtils.convertEventToEventDto(eventOpt.get(), eventUrls);
    }

    public EventResponse getEventByPage(int pageSize, int page) {
        Sort sort = Sort.by("startTime").descending();

        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);

        Page<Event> eventPage = eventRepository.findAll(pageable);
        var events = eventPage.getContent();
        List<Integer> eventId = events.stream().map(Event::getEventId).toList();

        List<EventImage> eventImages = eventImageService.findAllByEventIdIn(eventId);

        List<EventDto> result = new ArrayList<>();

        for (int i = 0; i < eventId.size(); i++) {
            Event event = events.get(i);
            List<String> imageUrls = eventImages.stream().filter(eventImage -> eventImage.getEventId() == event.getEventId())
                            .map(EventImage::getImageUrl)
                            .collect(Collectors.toList());

            result.add(EventUtils.convertEventToEventDto(event, imageUrls));
        }

        long total = eventPage.getTotalElements();
        return EventResponse.builder().eventDtos(result).total(total).build();
    }

    @Transactional
    public Event createEvent(EventRequest eventRequest, List<MultipartFile> images) {
        Event event = EventUtils.convertEventRequestToEvent(eventRequest);
        Event savedEvent = eventRepository.save(event);
        eventImageService.saveListEventImage(savedEvent.getEventId(), images);
        return savedEvent;
    }
}
