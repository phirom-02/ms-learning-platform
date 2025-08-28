package com.firom.lms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveCourseRequest {
    private Integer id;

    @NotEmpty(message = "Course title is required")
    private String title;

    @NotEmpty(message = "Description title is required")
    private String description;

    @NotNull(message = "Course category is required")
    private Integer categoryId;
}
