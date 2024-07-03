package com.app.final_project.eventImage;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(EventImageId.class)
public class EventImage {
    @Id
    @Column(name = "event_id")
    private int eventId;

    @Id
    @Column(name = "image_url", length = 255)
    private String imageUrl;
}
