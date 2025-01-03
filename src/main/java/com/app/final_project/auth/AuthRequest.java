package com.app.final_project.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    @NotBlank(message = "Username is not null or empty")
    String username;
    @NotBlank(message = "Password is not null or empty")
    String password;
}
