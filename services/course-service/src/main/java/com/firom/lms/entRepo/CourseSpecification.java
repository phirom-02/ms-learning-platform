package com.firom.lms.entRepo;

import com.firom.lms.constants.CourseStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class CourseSpecification {

    public static Specification<Course> unrestricted() {
        return Specification.unrestricted();
    }

    public static Specification<Course> titleContains(String title) {
        return (root, query, cb) ->
                title == null || title.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Course> statusEquals(CourseStatus status) {
        return (root, query, cb) ->
                status == null
                        ? null
                        : cb.equal(root.get("status"), status);
    }

    public static Specification<Course> instructorIdEquals(UUID instructorId) {
        return (root, query, cb) ->
                instructorId == null
                        ? null
                        : cb.equal(root.get("instructorId"), instructorId);
    }
}
