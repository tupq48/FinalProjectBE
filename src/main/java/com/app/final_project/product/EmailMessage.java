package com.app.final_project.product;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class EmailMessage {
    private String to;
    private String subject;
    private String body;
}
