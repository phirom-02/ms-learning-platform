package com.firom.lms.controllers;

import com.firom.lms.services.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses/{course-id}/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;
}
