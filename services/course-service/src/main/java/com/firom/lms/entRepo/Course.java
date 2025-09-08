package com.firom.lms.entRepo;

import com.firom.lms.constants.CourseStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private CourseStatus status;

    private UUID instructorId;

// Will be added back later
//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    private Category category;
//
//    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
//    private List<Lesson> lessons;
}
