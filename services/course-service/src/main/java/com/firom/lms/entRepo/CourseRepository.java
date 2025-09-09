package com.firom.lms.entRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID>, JpaSpecificationExecutor<Course> {

    Optional<Course> findByIdAndInstructorId(UUID courseId, UUID instructorId);

}
