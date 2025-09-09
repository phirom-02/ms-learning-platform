package com.firom.lms.dto.mapper;

import com.firom.lms.dto.request.EnrollmentRequest;
import com.firom.lms.dto.response.EnrollmentResponse;
import com.firom.lms.entRepo.Enrollment;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {

    public Enrollment enrollmentRequestToEntity(EnrollmentRequest request) {
        return Enrollment.builder()
                .courseId(request.getCourseId())
                .studentId(request.getStudentId())
                .build();
    }

    public EnrollmentResponse entityToEnrollmentResponse(Enrollment enrollment) {
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .courseId(enrollment.getCourseId())
                .studentId(enrollment.getStudentId())
                .enrolledAt(enrollment.getEnrolledAt())
                .build();
    }
}
