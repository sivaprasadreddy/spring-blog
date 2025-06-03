package com.sivalabs.springblog.domain.services;

import com.sivalabs.springblog.domain.data.CategoryRepository;
import com.sivalabs.springblog.domain.models.Category;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category getOrCreateCategoryByName(String name) {
        return categoryRepository.getOrCreateCategoryByName(name);
    }
}
