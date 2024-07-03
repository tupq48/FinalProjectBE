package com.app.final_project.registration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AttendanceImage {
    String uploadImageUrl;
    String trainImageUrl;

}
