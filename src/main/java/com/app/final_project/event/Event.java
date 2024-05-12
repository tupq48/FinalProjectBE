package com.app.final_project.event;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventId;
    private int point;
    private int maxAttenders;
    private String eventName;
    private String location;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isDeleted = false;
}
