package com.app.final_project.user;

import com.app.final_project.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "user_image")
public class UserImage {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private int userId;
        private String urlImage;
        private String urlRoundFaceImage;
}
