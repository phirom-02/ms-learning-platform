package com.firom.lms.controllers;

import com.firom.lms.dto.request.SaveLessonRequest;
import com.firom.lms.dto.response.ApiResponse;
import com.firom.lms.dto.response.LessonResponse;
import com.firom.lms.services.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Not used currently
@RestController
@RequestMapping("/api/v1/courses/{course-id}/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    public ResponseEntity<ApiResponse<LessonResponse>> createLesson(@PathVariable("course-id") Integer courseId, @RequestBody @Valid SaveLessonRequest request) {
        ApiResponse<LessonResponse> response = new ApiResponse<>(lessonService.createLesson(courseId, request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LessonResponse>>> getLessons(@PathVariable("course-id") Integer courseId) {
        ApiResponse<List<LessonResponse>> response = new ApiResponse<>(lessonService.getLessons(courseId));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{lesson-id}")
    public ResponseEntity<ApiResponse<LessonResponse>> getLessonById(@PathVariable("course-id") Integer courseId, @PathVariable("lesson-id") Integer lessonId) {
        ApiResponse<LessonResponse> response = new ApiResponse<>(lessonService.getLessonById(courseId, lessonId));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{lesson-id}")
    public ResponseEntity<ApiResponse<LessonResponse>> updateLesson(@PathVariable("course-id") Integer courseId, @PathVariable("lesson-id") Integer lessonId, @RequestBody @Valid SaveLessonRequest request) {
        ApiResponse<LessonResponse> response = new ApiResponse<>(lessonService.updateLesson(courseId, lessonId, request));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{lesson-id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable("course-id") Integer courseId, @PathVariable("lesson-id") Integer lessonId) {
        lessonService.deleteLesson(courseId, lessonId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
