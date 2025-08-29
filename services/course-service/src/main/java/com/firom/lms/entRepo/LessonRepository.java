package com.firom.lms.entRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    List<Lesson> findByCourseId(Integer courseId);

    Optional<Lesson> findByIdAndCourseId(Integer lessonId, Integer courseId);
}
