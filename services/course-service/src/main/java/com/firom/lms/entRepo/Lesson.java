package com.firom.lms.entRepo;

import com.firom.lms.constants.LessonResourceType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "course")
public class Lesson {

    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Enumerated(EnumType.STRING)
    private LessonResourceType resourceType;
}
