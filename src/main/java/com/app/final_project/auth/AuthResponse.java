package com.app.final_project.auth;

import lombok.*;

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
}
