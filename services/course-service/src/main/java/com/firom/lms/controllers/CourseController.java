package com.firom.lms.controllers;

import com.firom.lms.dto.request.CourseResponse;
import com.firom.lms.dto.request.SaveCourseRequest;
import com.firom.lms.dto.response.ApiResponse;
import com.firom.lms.services.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(@RequestBody @Valid SaveCourseRequest request) {
        ApiResponse<CourseResponse> response = new ApiResponse<>(courseService.createCourse(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    private ResponseEntity<ApiResponse<List<CourseResponse>>> getAllCourses() {
        ApiResponse<List<CourseResponse>> response = new ApiResponse<>(courseService.getAllCourses());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{course-id}")
    private ResponseEntity<ApiResponse<CourseResponse>> getCourseById(@PathVariable("course-id") Integer courseId) {
        ApiResponse<CourseResponse> response = new ApiResponse<>(courseService.getCourseById(courseId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{course-id}")
    private ResponseEntity<ApiResponse<CourseResponse>> updateCourse(@PathVariable("course-id") Integer courseId, @RequestBody @Valid SaveCourseRequest request) {
        ApiResponse<CourseResponse> response = new ApiResponse<>(courseService.updateCourse(courseId, request));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{course-id}")
    public ResponseEntity<Void> deleteCourseById(@PathVariable("course-id") Integer courseId) {
        courseService.deleteCourseById(courseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
