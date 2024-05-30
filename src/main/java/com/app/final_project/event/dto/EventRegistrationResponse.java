package com.app.final_project.event.dto;

import com.app.final_project.registration.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRegistrationResponse {
    private int userId;
    private int eventId;
    private int point;
    private String eventName;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private RegistrationStatus status;
}
