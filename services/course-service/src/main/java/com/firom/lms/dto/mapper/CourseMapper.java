package com.firom.lms.dto.mapper;

import com.firom.lms.dto.request.CourseResponse;
import com.firom.lms.dto.request.SaveCourseRequest;
import com.firom.lms.entRepo.Category;
import com.firom.lms.entRepo.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public Course saveCourseRequestToEntity(SaveCourseRequest request, Category category) {
        return Course.builder()
                .id(request.getId())
                .title(request.getTitle())
                .description(request.getDescription())
                .category(category)
                .build();
    }

    public CourseResponse EntityToCourseResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .category(course.getCategory().getName())
                .build();
    }
}
