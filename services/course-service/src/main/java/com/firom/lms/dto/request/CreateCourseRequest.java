package com.firom.lms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourseRequest {
    @NotEmpty(message = "Course title is required")
    private String title;

    @NotEmpty(message = "Description is required")
    private String description;

    private UUID instructorId;
}
