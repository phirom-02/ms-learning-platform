package com.firom.lms.controllers;

import com.firom.lms.dto.request.CreateCourseRequest;
import com.firom.lms.dto.request.UpdateCourseRequest;
import com.firom.lms.dto.request.UpdateCourseStatusRequest;
import com.firom.lms.dto.response.ApiResponse;
import com.firom.lms.dto.response.CourseResponse;
import com.firom.lms.services.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/instructor/courses")
@RequiredArgsConstructor
public class CourseInstructorController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
            @RequestAttribute("userId") UUID instructorId,
            @RequestBody @Valid CreateCourseRequest request
    ) {
        ApiResponse<CourseResponse> response = new ApiResponse<>(courseService.createCourseByInstructor(instructorId, request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> getAllInstructorCourses(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "createdAt,DESC") String sort,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestAttribute("userId") UUID instructorId
    ) {

        ApiResponse<Page<CourseResponse>> response = new ApiResponse<>(courseService.getAllCourses(
                page,
                size,
                sort,
                title,
                instructorId,
                status
        ));

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{course-id}")
    private ResponseEntity<ApiResponse<CourseResponse>> getCourseById(
            @PathVariable("course-id") UUID courseId,
            @RequestAttribute("userId") UUID instructorId
    ) {
        ApiResponse<CourseResponse> response = new ApiResponse<>(courseService.getCourseByInstructor(courseId, instructorId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{course-id}")
    public ResponseEntity<Void> deleteCourseByIdAndInstructorId(
            @PathVariable("course-id") UUID courseId,
            @RequestAttribute("userId") UUID instructorId
    ) {
        courseService.deleteCourseByInstructor(courseId, instructorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{course-id}")
    private ResponseEntity<ApiResponse<CourseResponse>> updateCourse(
            @PathVariable("course-id") UUID courseId,
            @RequestAttribute("userId") UUID instructorId,
            @RequestBody @Valid UpdateCourseRequest request
    ) {
        ApiResponse<CourseResponse> response = new ApiResponse<>(courseService.updateCourseByInstructor(courseId, instructorId, request));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{course-id}/status")
    public ResponseEntity<ApiResponse<UUID>> updateCourseStatus(
            @PathVariable("course-id") UUID courseId,
            @RequestAttribute("userId") UUID instructorId,
            @RequestBody @Valid UpdateCourseStatusRequest request
    ) {
        ApiResponse<UUID> response = new ApiResponse<>(courseService.updateCourseStatusByInstructor(courseId, instructorId, request));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
