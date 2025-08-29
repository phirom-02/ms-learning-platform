package com.firom.lms.services.impl;

import com.firom.lms.dto.mapper.LessonMapper;
import com.firom.lms.dto.request.SaveLessonRequest;
import com.firom.lms.dto.response.LessonResponse;
import com.firom.lms.entRepo.Lesson;
import com.firom.lms.entRepo.LessonRepository;
import com.firom.lms.services.CourseService;
import com.firom.lms.services.LessonService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseService courseService;
    private final LessonMapper lessonMapper;

    @Override
    public LessonResponse createLesson(Integer courseId, SaveLessonRequest request) {
        var course = courseService.getCourseEntityById(courseId);
        var lessonToCreate = lessonMapper.saveLessonRequestToEntity(request, course);
        var lesson = lessonRepository.save(lessonToCreate);
        return lessonMapper.entityToLessonResponse(lesson);
    }

    @Override
    public List<LessonResponse> getLessons(Integer courseId) {
        courseService.getCourseEntityById(courseId);
        return lessonRepository.findByCourseId(courseId).stream()
                .map(lessonMapper::entityToLessonResponse)
                .toList();
    }

    @Override
    public LessonResponse getLessonById(Integer courseId, Integer lessonId) {
        courseService.getCourseEntityById(courseId);
        return lessonMapper.entityToLessonResponse(getLessonEntityById(courseId, lessonId));
    }

    @Override
    public LessonResponse updateLesson(Integer courseId, Integer lessonId, SaveLessonRequest request) {
        var course = courseService.getCourseEntityById(courseId);
        var lesson = getLessonEntityById(courseId, lessonId);
        var lessonToUpdate = lessonMapper.saveLessonRequestToEntity(request, course, lesson);
        var updatedLesson = lessonRepository.save(lessonToUpdate);
        return lessonMapper.entityToLessonResponse(updatedLesson);
    }

    @Override
    public void deleteLesson(Integer courseId, Integer lessonId) {
        courseService.getCourseEntityById(courseId);
        lessonRepository.deleteById(lessonId);
    }

    private Lesson getLessonEntityById(Integer courseId, Integer lessonId) {
        return lessonRepository.findByIdAndCourseId(lessonId, courseId)
                .orElseThrow(() -> new EntityNotFoundException("No lesson found with ID: " + lessonId));
    }
}
