package com.firom.lms.dto.request;

import com.firom.lms.constants.CourseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UpdateCourseStatusRequest {

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "PUBLISHED|PRIVATE", message = "status must be PUBLISHED or PRIVATE")
    private String status;
}
