package com.firom.lms.controllers;

import com.firom.lms.constants.CourseStatus;
import com.firom.lms.dto.response.CourseResponse;
import com.firom.lms.dto.request.CreateCourseRequest;
import com.firom.lms.dto.response.ApiResponse;
import com.firom.lms.services.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(@RequestBody @Valid CreateCourseRequest request) {
        ApiResponse<CourseResponse> response = new ApiResponse<>(courseService.createCourse(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    private ResponseEntity<ApiResponse<Page<CourseResponse>>> getAllCourses(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "createdAt,DESC") String sort,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String instructorId
    ) {
        ApiResponse<Page<CourseResponse>> response = new ApiResponse<>(courseService.getAllCourses(page, size, sort, title, instructorId, CourseStatus.PUBLISHED.toString()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{course-id}")
    private ResponseEntity<ApiResponse<CourseResponse>> getCourseById(@PathVariable("course-id") String courseId) {
        ApiResponse<CourseResponse> response = new ApiResponse<>(courseService.getCourseById(courseId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{course-id}")
    private ResponseEntity<ApiResponse<CourseResponse>> updateCourse(@PathVariable("course-id") String courseId, @RequestBody @Valid CreateCourseRequest request) {
        ApiResponse<CourseResponse> response = new ApiResponse<>(courseService.updateCourse(courseId, request));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{course-id}")
    public ResponseEntity<Void> deleteCourseById(@PathVariable("course-id") String courseId) {
        courseService.deleteCourseById(courseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
