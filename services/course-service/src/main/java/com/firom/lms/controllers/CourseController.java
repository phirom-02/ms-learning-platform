package com.firom.lms.controllers;

import com.firom.lms.constants.CourseStatus;
import com.firom.lms.dto.response.ApiResponse;
import com.firom.lms.dto.response.CourseResponse;
import com.firom.lms.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    private ResponseEntity<ApiResponse<Page<CourseResponse>>> getAllCourses(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "createdAt,DESC") String sort,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) UUID instructorId
    ) {
        ApiResponse<Page<CourseResponse>> response = new ApiResponse<>(courseService.getAllCourses(
                page,
                size,
                sort,
                title,
                instructorId,
                CourseStatus.PUBLISHED.toString())
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{course-id}")
    private ResponseEntity<ApiResponse<CourseResponse>> getCourseById(@PathVariable("course-id") UUID courseId) {
        ApiResponse<CourseResponse> response = new ApiResponse<>(courseService.getCourseById(courseId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
