package com.firom.lms.services;

import com.firom.lms.dto.response.CourseResponse;
import com.firom.lms.dto.request.CreateCourseRequest;
import com.firom.lms.entRepo.Course;
import org.springframework.data.domain.Page;

public interface CourseService {
    CourseResponse createCourse(CreateCourseRequest request);

    CourseResponse getCourseById(String courseId);

    void deleteCourseById(String courseId);

    CourseResponse updateCourse(String courseId, CreateCourseRequest request);

    Course getCourseEntityById(String courseId);

    Page<CourseResponse> getAllCourses(int page, int size, String sort, String title, String instructorId, String status);
}
