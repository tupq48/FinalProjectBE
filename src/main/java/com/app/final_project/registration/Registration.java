package com.app.final_project.registration;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer registrationId;
    Integer eventId;
    Integer userId;
    LocalDateTime registrationTime;
    @Enumerated(EnumType.STRING)
    RegistrationStatus status;
    String imageUrl;
    boolean isAIPredicted;
}
