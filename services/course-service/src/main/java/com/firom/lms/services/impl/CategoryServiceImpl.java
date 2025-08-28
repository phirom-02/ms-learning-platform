package com.firom.lms.services.impl;

import com.firom.lms.entRepo.Category;
import com.firom.lms.entRepo.CategoryRepository;
import com.firom.lms.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryEntityById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No Category found with id: " + id));
    }
}
