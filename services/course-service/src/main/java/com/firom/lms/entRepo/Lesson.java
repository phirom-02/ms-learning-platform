package com.firom.lms.entRepo;

import com.firom.lms.constants.LessonResourceType;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "course_id", updatable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type")
    private LessonResourceType resourceType;
}
