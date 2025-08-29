package com.firom.lms.services;

import com.firom.lms.dto.request.CourseResponse;
import com.firom.lms.dto.request.SaveCourseRequest;
import com.firom.lms.entRepo.Course;

import java.util.List;

public interface CourseService {
    CourseResponse createCourse(SaveCourseRequest request);

    CourseResponse getCourseById(Integer courseId);

    List<CourseResponse> getAllCourses();

    void deleteCourseById(Integer courseId);

    CourseResponse updateCourse(Integer courseId, SaveCourseRequest request);

    Course getCourseEntityById(Integer courseId);
}
