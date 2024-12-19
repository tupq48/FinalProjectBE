package com.app.final_project.auth;

import com.app.final_project.enums.RoleType;
import jakarta.persistence.Enumerated;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private int id;
    private String username;
    private String fullName;
    private String accessToken;
    private String refreshToken;
    @Enumerated(STRING)
    private RoleType role;
}
