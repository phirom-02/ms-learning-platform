package com.firom.lms.services.impl;

import com.firom.lms.InvalidCourseStatusException;
import com.firom.lms.constants.CourseStatus;
import com.firom.lms.dto.mapper.CourseMapper;
import com.firom.lms.dto.request.CreateCourseRequest;
import com.firom.lms.dto.response.CourseResponse;
import com.firom.lms.entRepo.Course;
import com.firom.lms.entRepo.CourseRepository;
import com.firom.lms.entRepo.CourseSpecification;
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
    public CourseResponse createCourse(CreateCourseRequest request) {
        var courseToCreate = courseMapper.createCourseRequestToEntity(request);
        var course = courseRepository.save(courseToCreate);
        return courseMapper.entityToCourseResponse(course);
    }

    @Override
    public CourseResponse getCourseById(String courseId) {
        return courseMapper.entityToCourseResponse(getCourseEntityById(courseId));
    }

    @Override
    public Page<CourseResponse> getAllCourses(int page, int size, String sort, String title, String instructorId, String status) {
        // Parse status
        CourseStatus parsedStatus;
        try {
            parsedStatus = CourseStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new InvalidCourseStatusException("Invalid course status: " + status);
        }

        UUID parsedInstructorId = instructorId == null
                ? null
                : UUID.fromString(instructorId);

        // Parse sort. e.g. createdAt,DESC or createdAt,desc
        String[] sortParts = sort.split(",");
        Sort.Direction sortDirection = Sort.Direction
                .fromOptionalString(sortParts[1].toUpperCase())
                .orElse(Sort.Direction.ASC);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortParts[0]));

        Specification<Course> spec = CourseSpecification.unrestricted()
                .and(CourseSpecification.titleContains(title))
                .and(CourseSpecification.statusEquals(parsedStatus))
                .and(CourseSpecification.instructorIdEquals(parsedInstructorId));

        // Query courses data + pagination
        Page<Course> courses = courseRepository.findAll(spec, pageable);

        List<CourseResponse> response = courses.getContent().stream()
                .map(courseMapper::entityToCourseResponse)
                .toList();

        return new PageImpl<>(response, pageable, courses.getTotalElements());
    }

    @Override
    public void deleteCourseById(String courseId) {
        getCourseEntityById(courseId);
        courseRepository.deleteById(UUID.fromString(courseId));
    }

    @Override
    public CourseResponse updateCourse(String courseId, CreateCourseRequest request) {
        var course = getCourseEntityById(courseId);
        var courseToUpdate = courseMapper.createCourseRequestToEntity(request, course);
        var updatedCourse = courseRepository.save(courseToUpdate);
        return courseMapper.entityToCourseResponse(updatedCourse);
    }

    @Override
    public Course getCourseEntityById(String courseId) {
        return courseRepository.findById(UUID.fromString(courseId))
                .orElseThrow(() -> new EntityNotFoundException("No course found with Id: " + courseId));
    }
}
