package com.firom.lms.services.impl;

import com.firom.lms.dto.mapper.EnrollmentMapper;
import com.firom.lms.dto.request.EnrollmentRequest;
import com.firom.lms.dto.response.ApiResponse;
import com.firom.lms.dto.response.EnrollmentResponse;
import com.firom.lms.entRepo.Course;
import com.firom.lms.entRepo.Enrollment;
import com.firom.lms.entRepo.EnrollmentRepository;
import com.firom.lms.entRepo.User;
import com.firom.lms.exception.EnrollmentException;
import com.firom.lms.producers.EnrollmentMessage;
import com.firom.lms.producers.EnrollmentProducer;
import com.firom.lms.remotes.client.CourseClient;
import com.firom.lms.remotes.client.UserClient;
import com.firom.lms.services.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentMapper enrollmentMapper;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseClient courseClient;
    private final UserClient userClient;
    private final EnrollmentProducer enrollmentProducer;

    @Transactional
    @Override
    public EnrollmentResponse enrollCourse(UUID studentId, EnrollmentRequest request) {
        request.setStudentId(studentId);
        // Check if student already enrolled the course
        Optional<Enrollment> retrievedEnrollment = enrollmentRepository.findByStudentIdAndAndCourseId(studentId, request.getCourseId());
        if (retrievedEnrollment.isPresent()) {
            throw new EnrollmentException("User already enrolled the course");
        }

        // Retrieve user(student)
        ResponseEntity<ApiResponse<User>> studentResponse = userClient.getUserById(studentId);
        User student = Objects.requireNonNull(studentResponse.getBody()).getData();

        // Retrieve course
        ResponseEntity<ApiResponse<Course>> getCourseByIdResponse = courseClient.getCourseById(request.getCourseId());
        Course course = Objects.requireNonNull(getCourseByIdResponse.getBody()).getData();

        // Retrieve instructor


        ResponseEntity<ApiResponse<User>> instructorResponse = userClient.getUserById(request.getStudentId());
        User instructor = instructorResponse.getBody().getData();

        // Set studentId
        request.setStudentId(student.getId());
        request.setCourseId(course.getId());

        // Map enrollment request to entity
        Enrollment enrollmentToSave = enrollmentMapper.enrollmentRequestToEntity(request);

        // Persist to db
        Enrollment enrollment = enrollmentRepository.save(enrollmentToSave);

        // Send enrollment notice to Notification service
        enrollmentProducer.sendEnrollmentNotice(
                EnrollmentMessage.builder()
                        .courseTitle(course.getTitle())
                        .instructorUsername(instructor.getUsername())
                        .studentUsername(student.getUsername())
                        .studentEmail(student.getEmail())
                        .build()
        );

        // Map then return
        return enrollmentMapper.entityToEnrollmentResponse(enrollment);
    }

    @Override
    public Page<EnrollmentResponse> getStudentCourseEnrollments(int page, int size, String sort, UUID studentId) {
        String[] sortParts = sort.split(",");
        Sort.Direction sortDirection = Sort.Direction
                .fromOptionalString(sortParts[1].toUpperCase())
                .orElse(Sort.Direction.ASC);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortParts[0]));

        Page<Enrollment> courses = enrollmentRepository.findAll(pageable);

        List<EnrollmentResponse> response = courses.getContent().stream()
                .map(enrollmentMapper::entityToEnrollmentResponse)
                .toList();

        return new PageImpl<>(response, pageable, courses.getTotalElements());
    }
}
