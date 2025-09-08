package com.firom.lms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Not used currently
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveLessonRequest {

    private Integer id;

    @NotEmpty(message = "Lesson title is required")
    private String title;

    @NotEmpty(message = "Lesson description is required")
    private String description;

    @NotNull(message = "Lesson courseId is required")
    private Integer courseId;

    @NotEmpty(message = "Lesson resource type is required")
    private String resourceType;
}
