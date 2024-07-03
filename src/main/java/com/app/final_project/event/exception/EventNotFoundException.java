package com.app.final_project.event.exception;

public class EventNotFoundException extends RuntimeException {
    int eventId;
    public EventNotFoundException(int eventId) {
        super("Not found event with ID: " + eventId);
    }

    public EventNotFoundException(String message) {
        super(message);
    }

    public EventNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
