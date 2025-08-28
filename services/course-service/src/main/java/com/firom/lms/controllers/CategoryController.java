package com.firom.lms.controllers;

import com.firom.lms.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
}
