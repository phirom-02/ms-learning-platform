package com.firom.lms.services;

import com.firom.lms.dto.request.SaveLessonRequest;
import com.firom.lms.dto.response.LessonResponse;

import java.util.List;

public interface LessonService {
    LessonResponse createLesson(Integer courseId, SaveLessonRequest request);

    List<LessonResponse> getLessons(Integer courseId);

    LessonResponse getLessonById(Integer courseId, Integer lessonId);

    LessonResponse updateLesson(Integer courseId, Integer lessonId, SaveLessonRequest request);

    void deleteLesson(Integer courseId, Integer lessonId);
}
