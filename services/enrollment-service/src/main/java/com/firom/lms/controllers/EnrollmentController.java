package com.firom.lms.controllers;

import com.firom.lms.dto.request.EnrollmentRequest;
import com.firom.lms.dto.response.ApiResponse;
import com.firom.lms.dto.response.EnrollmentResponse;
import com.firom.lms.services.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<EnrollmentResponse>> enrollCourse(
            @RequestAttribute("userId") UUID userId,
            @RequestBody @Valid EnrollmentRequest request
    ) {
        ApiResponse<EnrollmentResponse> response = new ApiResponse<>(enrollmentService.enrollCourse(userId, request));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<EnrollmentResponse>>> getStudentCourseEnrollments(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "enrolledAt,DESC") String sort,
            @RequestAttribute("userId") UUID studentId
    ) {
        ApiResponse<Page<EnrollmentResponse>> response = new ApiResponse<>(
                enrollmentService.getStudentCourseEnrollments(
                        page,
                        size,
                        sort,
                        studentId
                )
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
