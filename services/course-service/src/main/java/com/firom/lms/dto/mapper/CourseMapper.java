package com.firom.lms.dto.mapper;

import com.firom.lms.constants.CourseStatus;
import com.firom.lms.dto.request.CreateCourseRequest;
import com.firom.lms.dto.request.UpdateCourseRequest;
import com.firom.lms.dto.response.CourseResponse;
import com.firom.lms.entRepo.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public Course createCourseRequestToEntity(CreateCourseRequest request) {
        return Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(CourseStatus.PUBLISHED)
                .instructorId(request.getInstructorId())
                .build();
    }

    public Course updateCourseRequestToEntity(UpdateCourseRequest request, Course course) {
        course.setTitle(
                request.getTitle() != null && !request.getTitle().isBlank()
                        ? request.getTitle()
                        : null
        );
        course.setDescription(
                request.getDescription() != null && !request.getDescription().isBlank()
                        ? request.getDescription()
                        : null
        );
        course.setStatus(course.getStatus());
        return course;
    }

    public CourseResponse entityToCourseResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .status(course.getStatus())
                .instructorId(course.getInstructorId())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}
