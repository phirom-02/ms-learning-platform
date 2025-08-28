package com.firom.lms.services.impl;

import com.firom.lms.dto.mapper.CourseMapper;
import com.firom.lms.dto.request.CourseResponse;
import com.firom.lms.dto.request.SaveCourseRequest;
import com.firom.lms.entRepo.CourseRepository;
import com.firom.lms.services.CategoryService;
import com.firom.lms.services.CourseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CategoryService categoryService;
    private final CourseMapper courseMapper;

    @Override
    public CourseResponse createCourse(SaveCourseRequest request) {
        var category = categoryService.getCategoryEntityById(request.getCategoryId());
        var courseToCreate = courseMapper.saveCourseRequestToEntity(request, category);
        var course = courseRepository.save(courseToCreate);
        return courseMapper.EntityToCourseResponse(course);
    }

    @Override
    public CourseResponse getCourseById(Integer courseId) {
        return courseRepository.findById(courseId)
                .map(courseMapper::EntityToCourseResponse)
                .orElseThrow(() -> new EntityNotFoundException("No course found with Id: " + courseId));
    }

    @Override
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream().map(courseMapper::EntityToCourseResponse)
                .toList();
    }

    @Override
    public void deleteCourseById(Integer courseId) {
        getCourseById(courseId);
        courseRepository.deleteById(courseId);
    }

    @Override
    public CourseResponse updateCourse(Integer courseId, SaveCourseRequest request) {
        getCourseById(courseId);
        var category = categoryService.getCategoryEntityById(request.getCategoryId());
        var courseToUpdate = courseMapper.saveCourseRequestToEntity(request, category);
        var course = courseRepository.save(courseToUpdate);
        return courseMapper.EntityToCourseResponse(course);
    }
}
