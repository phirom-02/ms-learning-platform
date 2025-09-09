package com.firom.lms.services;

import com.firom.lms.dto.request.EnrollmentRequest;
import com.firom.lms.dto.response.EnrollmentResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface EnrollmentService {

    EnrollmentResponse enrollCourse(UUID studentId, EnrollmentRequest request);

    Page<EnrollmentResponse> getStudentCourseEnrollments(int page, int size, String sort, UUID studentId);
}
