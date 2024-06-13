package com.app.final_project.notification;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer eventId;
    private Integer userId;
    private String title;
    private String description;
    private String avatar; // that image event url
    private String type = null;
    private LocalDateTime createdAt;
    private Boolean isUnRead = true;
}
