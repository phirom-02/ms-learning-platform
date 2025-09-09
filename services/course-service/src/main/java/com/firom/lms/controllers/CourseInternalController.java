package com.firom.lms.controllers;

import com.firom.lms.dto.response.ApiResponse;
import com.firom.lms.entRepo.Course;
import com.firom.lms.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses/internal/")
@RequiredArgsConstructor
public class CourseInternalController {

    private final CourseService courseService;

    @GetMapping("/{course-id}")
    public ResponseEntity<ApiResponse<Course>> getCourseById(@PathVariable("course-id") UUID courseId) {
        ApiResponse<Course> response = new ApiResponse<>(courseService.getCourseEntityById(courseId));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
