package com.firom.lms.services.impl;

import com.firom.lms.constants.CourseStatus;
import com.firom.lms.dto.mapper.CourseMapper;
import com.firom.lms.dto.request.CreateCourseRequest;
import com.firom.lms.dto.request.UpdateCourseRequest;
import com.firom.lms.dto.request.UpdateCourseStatusRequest;
import com.firom.lms.dto.response.CourseResponse;
import com.firom.lms.entRepo.Course;
import com.firom.lms.entRepo.CourseRepository;
import com.firom.lms.entRepo.CourseSpecification;
import com.firom.lms.exceptions.InvalidCourseStatusException;
import com.firom.lms.services.CourseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public CourseResponse createCourseByInstructor(CreateCourseRequest request) {
        var courseToCreate = courseMapper.createCourseRequestToEntity(request);
        var course = courseRepository.save(courseToCreate);
        return courseMapper.entityToCourseResponse(course);
    }

    @Override
    public CourseResponse createCourseByInstructor(UUID instructorId, CreateCourseRequest request) {
        request.setInstructorId(instructorId);
        var courseToCreate = courseMapper.createCourseRequestToEntity(request);
        var course = courseRepository.save(courseToCreate);
        return courseMapper.entityToCourseResponse(course);
    }

    @Override
    public CourseResponse getCourseById(UUID courseId) {
        return courseMapper.entityToCourseResponse(getCourseEntityById(courseId));
    }

    @Override
    public Page<CourseResponse> getAllCourses(
            int page,
            int size,
            String sort,
            String title,
            UUID instructorId,
            String status
    ) {
        // Parse status
        CourseStatus parsedStatus;
        try {
            parsedStatus = status == null
                    ? null
                    : CourseStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new InvalidCourseStatusException("Invalid course status: " + status);
        }

        // Parse sort. e.g. createdAt,DESC or createdAt,desc
        String[] sortParts = sort.split(",");
        Sort.Direction sortDirection = Sort.Direction
                .fromOptionalString(sortParts[1].toUpperCase())
                .orElse(Sort.Direction.ASC);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortParts[0]));

        Specification<Course> spec = CourseSpecification.unrestricted()
                .and(CourseSpecification.titleContains(title))
                .and(CourseSpecification.statusEquals(parsedStatus))
                .and(CourseSpecification.instructorIdEquals(instructorId));

        // Query courses data + pagination
        Page<Course> courses = courseRepository.findAll(spec, pageable);

        List<CourseResponse> response = courses.getContent().stream()
                .map(courseMapper::entityToCourseResponse)
                .toList();

        return new PageImpl<>(response, pageable, courses.getTotalElements());
    }

    @Override
    public void deleteCourseById(UUID courseId) {
        getCourseEntityById(courseId);
        courseRepository.deleteById(courseId);
    }

    @Override
    public void deleteCourseByInstructor(UUID courseId, UUID instructorId) {
        getCourseEntityByIdAndInstructorId(courseId, instructorId);
        courseRepository.deleteById(courseId);
    }

    @Override
    public CourseResponse updateCourseByInstructor(UUID courseId, UUID instructorId, UpdateCourseRequest request) {
        var course = getCourseEntityById(courseId);
        var courseToUpdate = courseMapper.updateCourseRequestToEntity(request, course);
        var updatedCourse = courseRepository.save(courseToUpdate);
        return courseMapper.entityToCourseResponse(updatedCourse);
    }

    @Override
    public CourseResponse updateCourseById(UUID courseId, UpdateCourseRequest request) {
        var course = getCourseEntityById(courseId);
        var courseToUpdate = courseMapper.updateCourseRequestToEntity(request, course);
        var updatedCourse = courseRepository.save(courseToUpdate);
        return courseMapper.entityToCourseResponse(updatedCourse);
    }

    @Override
    public Course getCourseEntityById(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("No course found with Id: " + courseId));
    }

    @Override
    public Course getCourseEntityByIdAndInstructorId(UUID courseId, UUID instructorId) {
        return courseRepository.findByIdAndInstructorId(courseId, instructorId)
                .orElseThrow(() -> new EntityNotFoundException("No course found with Id: " + courseId));
    }

    @Override
    public CourseResponse getCourseByInstructor(UUID courseId, UUID instructorId) {
        return courseMapper.entityToCourseResponse(getCourseEntityByIdAndInstructorId(courseId, instructorId));
    }

    @Override
    public UUID updateCourseStatusByInstructor(UUID courseId, UUID instructorId, UpdateCourseStatusRequest request) {
        Course course = getCourseEntityByIdAndInstructorId(courseId, instructorId);
        course.setStatus(CourseStatus.valueOf(request.getStatus()));
        courseRepository.save(course);
        return course.getId();
    }
}
