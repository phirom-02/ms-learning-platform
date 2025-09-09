package com.firom.lms.services;

import com.firom.lms.dto.request.CreateCourseRequest;
import com.firom.lms.dto.request.UpdateCourseRequest;
import com.firom.lms.dto.request.UpdateCourseStatusRequest;
import com.firom.lms.dto.response.CourseResponse;
import com.firom.lms.entRepo.Course;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface CourseService {

    CourseResponse createCourseByInstructor(CreateCourseRequest request);

    CourseResponse getCourseById(UUID courseId);

    void deleteCourseById(UUID courseId);

    CourseResponse updateCourseById(UUID courseId, UpdateCourseRequest request);

    CourseResponse updateCourseByInstructor(UUID courseId, UUID instructorId, UpdateCourseRequest request);

    Course getCourseEntityById(UUID courseId);

    Page<CourseResponse> getAllCourses(int page, int size, String sort, String title, UUID instructorId, String status);

    Course getCourseEntityByIdAndInstructorId(UUID courseId, UUID instructorId);

    CourseResponse createCourseByInstructor(UUID instructorId, CreateCourseRequest request);

    CourseResponse getCourseByInstructor(UUID courseId, UUID instructorId);

    UUID updateCourseStatusByInstructor(UUID courseId, UUID instructorId, UpdateCourseStatusRequest request);

    void deleteCourseByInstructor(UUID courseId, UUID instructorId);

}
