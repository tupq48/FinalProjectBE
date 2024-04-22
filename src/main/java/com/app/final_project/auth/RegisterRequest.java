package com.app.final_project.auth;

import com.app.final_project.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "Name User is not null or empty")
    @Pattern(regexp = "^[\\p{L}\\s]*$", message = "Name must not contain numbers or special characters")
    @Length(min = 1, max = 100)
    private String name;

    @Email
    @NotBlank(message = "Email is not null or empty")
    private String gmail;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_.-]{5,50}$", message = "Invalid username format")
    private String username;
    @NotBlank(message = "Password is not null or empty")
    @Length(min = 6, max = 50)
    private String password;
    @NotBlank(message = "Phone is not null or empty")
    @Length(min = 10, max = 10, message = "Phone number must be 10 characters long")
    private String phoneNumber;
    @NonNull
    private Gender gender;


}
