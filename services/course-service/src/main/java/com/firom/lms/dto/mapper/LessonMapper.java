package com.firom.lms.dto.mapper;

import com.firom.lms.constants.LessonResourceType;
import com.firom.lms.dto.request.SaveLessonRequest;
import com.firom.lms.dto.response.LessonResponse;
import com.firom.lms.entRepo.Course;
import com.firom.lms.entRepo.Lesson;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {

    public Lesson saveLessonRequestToEntity(SaveLessonRequest request, Course course) {
        return Lesson.builder()
                .id(request.getId())
                .title(request.getTitle())
                .description(request.getDescription())
                .resourceType(LessonResourceType.valueOf(request.getResourceType()))
                .course(course)
                .build();
    }

    public Lesson saveLessonRequestToEntity(SaveLessonRequest request, Course course, Lesson lesson) {
        lesson.setTitle(request.getTitle());
        lesson.setDescription(request.getDescription());
        lesson.setResourceType(LessonResourceType.valueOf(request.getResourceType()));
        lesson.setCourse(course);
        return lesson;
    }

    public LessonResponse entityToLessonResponse(Lesson lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .resourceType(lesson.getResourceType())
                .courseId(lesson.getCourse().getId())
                .build();
    }
}
