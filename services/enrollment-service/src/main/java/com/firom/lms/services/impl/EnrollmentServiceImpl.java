package com.firom.lms.services.impl;

import com.firom.lms.dto.mapper.EnrollmentMapper;
import com.firom.lms.dto.request.EnrollmentRequest;
import com.firom.lms.dto.response.ApiResponse;
import com.firom.lms.dto.response.EnrollmentResponse;
import com.firom.lms.entRepo.Course;
import com.firom.lms.entRepo.Enrollment;
import com.firom.lms.entRepo.EnrollmentRepository;
import com.firom.lms.entRepo.User;
import com.firom.lms.exception.CourseEnrollmentException;
import com.firom.lms.remotes.client.CourseClient;
import com.firom.lms.remotes.client.UserClient;
import com.firom.lms.services.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentServiceImpl.class);
    private final EnrollmentMapper enrollmentMapper;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseClient courseClient;
    private final UserClient userClient;

    @Transactional
    @Override
    public EnrollmentResponse enrollCourse(UUID studentId, EnrollmentRequest request) {
        // Check if student already enrolled the course
        Optional<Enrollment> retrievedEnrollment = enrollmentRepository.findByStudentIdAndAndCourseId(studentId, request.getCourseId());
        if (retrievedEnrollment.isPresent()) {
            throw new CourseEnrollmentException("User already enrolled the course");
        }

        // Retrieve user
        ResponseEntity<ApiResponse<User>> getUserByIdResponse = userClient.getUserById(studentId);
        User user = Objects.requireNonNull(getUserByIdResponse.getBody()).getData();

        // Retrieve course
        ResponseEntity<ApiResponse<Course>> getCourseByIdResponse = courseClient.getCourseById(request.getCourseId());
        Course course = Objects.requireNonNull(getCourseByIdResponse.getBody()).getData();

        // Set studentId
        request.setStudentId(user.getId());
        request.setCourseId(course.getId());

        // Map enrollment request to entity
        Enrollment enrollmentToSave = enrollmentMapper.enrollmentRequestToEntity(request);

        // Persist to db
        Enrollment enrollment = enrollmentRepository.save(enrollmentToSave);

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
