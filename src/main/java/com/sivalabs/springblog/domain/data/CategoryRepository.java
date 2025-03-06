package com.sivalabs.springblog.domain.data;

import com.sivalabs.springblog.domain.models.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category create(Category category);

    Optional<Category> findById(Long id);

    List<Category> findAll();

    void update(Category category);

    void deleteById(Long id);
}
