package com.app.final_project.event;

import com.app.final_project.event.dto.EventDto;
import com.app.final_project.event.dto.EventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("api/event")
public class EventController {


    @Autowired
    EventService eventService;

    @GetMapping()
    public ResponseEntity<?> getEventByPage(
            @RequestParam(defaultValue = "8") Integer pageSize,
            @RequestParam(defaultValue = "1") Integer page
    ){

        var eventDtos = eventService.getEventByPage(pageSize, page);
        return ResponseEntity.ok(eventDtos);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEventById(@PathVariable(required = true) Integer eventId) {
        EventDto result = eventService.findEventById(eventId);
        return ResponseEntity.ok(result);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createEvent(EventRequest eventRequest,
                                         @RequestParam("images")List<MultipartFile> images) {
        Event savedEvent = eventService.createEvent(eventRequest, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }
}
