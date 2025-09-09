package com.firom.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCourseRequest {
    private UUID id;

    @NotBlank(message = "Course title is required")
    private String title;

    @NotBlank(message = "Description title is required")
    private String description;
}
