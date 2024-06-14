package com.app.final_project.user.dto;

import com.app.final_project.enums.Gender;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class TopUsersByEventPoints {
    private int id;
    private String name;
    private String urlImage;
    private Gender gender;
    private Integer point;
    private Integer amountOfEvent;
    @Email()
    @Length(min = 1, max = 256)
    private String gmail;
}

