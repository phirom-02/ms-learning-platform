package com.firom.lms.remotes.client;

import com.firom.lms.dto.response.ApiResponse;
import com.firom.lms.entRepo.Course;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "course-service",
        url = "${application.remotes.course-url}"
)
public interface CourseClient {

    @GetMapping("/{course-id}")
    ResponseEntity<ApiResponse<Course>> getCourseById(@PathVariable("course-id") UUID courseId);

}
