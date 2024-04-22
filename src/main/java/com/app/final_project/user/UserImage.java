package com.app.final_project.user;

import com.app.final_project.enums.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "user_image")
public class UserImage {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int user_id;
        private String urlImage;
}
