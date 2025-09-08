package com.firom.lms.dto.mapper;

import com.firom.lms.constants.CourseStatus;
import com.firom.lms.dto.request.CourseResponse;
import com.firom.lms.dto.request.CreateCourseRequest;
import com.firom.lms.entRepo.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public Course createCourseRequestToEntity(CreateCourseRequest request) {
        return Course.builder()
                .id(request.getId())
                .title(request.getTitle())
                .description(request.getDescription())
                .status(CourseStatus.PUBLISHED)
                .build();
    }

    public Course createCourseRequestToEntity(CreateCourseRequest request, Course course) {
        course.setId(request.getId());
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        return course;
    }

    public CourseResponse EntityToCourseResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .build();
    }
}
