package com.firom.lms.services.impl;

import com.firom.lms.dto.mapper.CourseMapper;
import com.firom.lms.dto.request.CourseResponse;
import com.firom.lms.dto.request.CreateCourseRequest;
import com.firom.lms.entRepo.Course;
import com.firom.lms.entRepo.CourseRepository;
import com.firom.lms.services.CourseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public CourseResponse createCourse(CreateCourseRequest request) {
        var courseToCreate = courseMapper.createCourseRequestToEntity(request);
        var course = courseRepository.save(courseToCreate);
        return courseMapper.EntityToCourseResponse(course);
    }

    @Override
    public CourseResponse getCourseById(Integer courseId) {
        return courseMapper.EntityToCourseResponse(getCourseEntityById(courseId));
    }

    @Override
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream().map(courseMapper::EntityToCourseResponse)
                .toList();
    }

    @Override
    public void deleteCourseById(Integer courseId) {
        getCourseEntityById(courseId);
        courseRepository.deleteById(courseId);
    }

    @Override
    public CourseResponse updateCourse(Integer courseId, CreateCourseRequest request) {
        var course = getCourseEntityById(courseId);
        var courseToUpdate = courseMapper.createCourseRequestToEntity(request, course);
        var updatedCourse = courseRepository.save(courseToUpdate);
        return courseMapper.EntityToCourseResponse(updatedCourse);
    }

    @Override
    public Course getCourseEntityById(Integer courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("No course found with Id: " + courseId));
    }
}
