package com.firom.lms.producers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentMessage {
    private String courseTitle;
    private String studentEmail;
    private String studentUsername;
    private String instructorUsername;
}
