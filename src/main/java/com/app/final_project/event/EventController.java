package com.app.final_project.event;

import com.app.final_project.event.dto.EventDto;
import com.app.final_project.event.dto.EventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
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

    @PutMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateEvent(EventRequest eventRequest,
                                         @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        Event updatedEvent = eventService.updateEvent(eventRequest, images);
        return ResponseEntity.ok().body(updatedEvent);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteEvent(
            @RequestParam(value = "eventId") Integer eventId
    ){
        boolean isDeleted = eventService.deleteEvent(eventId);
        if (isDeleted)
            return ResponseEntity.ok("Event with id: " + eventId + " has been deleted.");
        return ResponseEntity.badRequest().body("An error occurred when deleting event with id: " + eventId);
    }
    @GetMapping("listEventAttended")
    public ResponseEntity<?> getEventAttended(
            @RequestParam("user_id") Integer userId){
        var listEventAttended = eventService.getEventAttended(userId);
        return ResponseEntity.ok(listEventAttended);
    }
    @GetMapping("getImagesUser")
    public ResponseEntity<?> getImagesUser(
            @RequestParam("userId") Integer userId,
            @RequestParam("eventId") Integer eventId)
    {
        var listImagesUser = eventService.getImagesUser(userId,eventId);
        return ResponseEntity.ok(listImagesUser);
    }
    @GetMapping("getEventByStatus")
    public ResponseEntity<?> getEventByStatus(
            @RequestParam(defaultValue = "8") Integer pageSize,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam("filter_by") Integer filterBy
    ){

        var eventDtos = eventService.getEventByStatus(pageSize, page, filterBy);
        return ResponseEntity.ok(eventDtos);
    }
}
