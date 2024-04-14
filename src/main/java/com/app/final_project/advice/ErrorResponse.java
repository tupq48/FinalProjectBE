package com.app.final_project.advice;

import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ErrorResponse {
    private int status;
    private Map<String, String> error;
    private String path;
    private Date timestamp = new Date();
    public String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(timestamp);
    }
}
