package com.firom.lms.dto.response;

import com.firom.lms.constants.LessonResourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonResponse {
    private Integer id;
    private String title;
    private String description;
    private Integer courseId;
    private LessonResourceType resourceType;
}
