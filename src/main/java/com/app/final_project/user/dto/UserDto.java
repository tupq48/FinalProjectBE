package com.app.final_project.user.dto;

import com.app.final_project.enums.Gender;
import com.app.final_project.enums.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder

public class UserDto {
    private int id;
    private String username;
    private String name;
    private String urlImage;
    private Gender gender;

    public UserDto(int id, String username, String name, String urlImage, Gender gender, LocalDateTime birthday, String phoneNumber, String gmail, RoleType role, boolean isLocked, boolean isEnabled) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.urlImage = urlImage;
        this.gender = gender;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.gmail = gmail;
        this.role = role;
        this.isLocked = isLocked;
        this.isEnabled=isEnabled;
    }

    private LocalDateTime birthday;
    @Pattern(regexp = "^[0-9]{10}$")
    private String phoneNumber;
    @Email()
    @Length(min = 1, max = 256)
    private String gmail;
    private RoleType role;
    private boolean isLocked;
    private boolean isEnabled = true;


    public UserDto() {

    }
}
