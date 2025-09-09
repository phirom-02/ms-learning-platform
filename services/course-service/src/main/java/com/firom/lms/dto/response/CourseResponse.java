package com.firom.lms.dto.response;

import com.firom.lms.constants.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {
    private UUID id;
    private String title;
    private String description;
    private CourseStatus status;
    private UUID instructorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
