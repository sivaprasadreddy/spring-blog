package com.sivalabs.springblog.domain.data;

import com.sivalabs.springblog.domain.models.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category create(Category category);

    Category getOrCreateCategoryByName(String name);

    Optional<Category> findById(Long id);

    Optional<Category> findBySlug(String slug);

    List<Category> findAll();

    void update(Category category);

    void deleteById(Long id);
}
