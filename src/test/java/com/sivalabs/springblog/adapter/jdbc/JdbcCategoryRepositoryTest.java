package com.sivalabs.springblog.adapter.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.springblog.TestcontainersConfig;
import com.sivalabs.springblog.domain.models.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Import(TestcontainersConfig.class)
@Sql("/test-data.sql")
class JdbcCategoryRepositoryTest {
    @Autowired
    private JdbcClient jdbcClient;

    private JdbcCategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository = new JdbcCategoryRepository(jdbcClient);
    }

    @Test
    void shouldCreateCategory() {
        Category category = new Category(null, "Test Category", "test-category");
        Category savedCategory = categoryRepository.create(category);
        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("Test Category");
        assertThat(savedCategory.getSlug()).isEqualTo("test-category");
    }

    @Test
    void shouldFindCategoryById() {
        var category = categoryRepository.findById(1L);
        assertThat(category).isPresent();
        assertThat(category.get().getName()).isEqualTo("Java");
        assertThat(category.get().getSlug()).isEqualTo("java");
    }

    @Test
    void shouldReturnEmptyWhenCategoryNotFound() {
        var category = categoryRepository.findById(999L);
        assertThat(category).isEmpty();
    }

    @Test
    void shouldUpdateCategory() {
        Category category = new Category(1L, "Updated Java", "updated-java");
        categoryRepository.update(category);

        var updatedCategory = categoryRepository.findById(1L);
        assertThat(updatedCategory).isPresent();
        assertThat(updatedCategory.get().getName()).isEqualTo("Updated Java");
        assertThat(updatedCategory.get().getSlug()).isEqualTo("updated-java");
    }

    @Test
    void shouldDeleteCategory() {
        // Create a new category first
        Category newCategory = new Category(null, "Test Delete", "test-delete");
        Category savedCategory = categoryRepository.create(newCategory);
        Long categoryId = savedCategory.getId();

        // Delete the category
        categoryRepository.deleteById(categoryId);

        // Verify it's deleted
        var category = categoryRepository.findById(categoryId);
        assertThat(category).isEmpty();
    }

    @Test
    void shouldFindCategoryBySlug() {
        var category = categoryRepository.findBySlug("java");
        assertThat(category).isPresent();
        assertThat(category.get().getId()).isEqualTo(1L);
        assertThat(category.get().getName()).isEqualTo("Java");
        assertThat(category.get().getSlug()).isEqualTo("java");
    }

    @Test
    void shouldReturnEmptyWhenCategorySlugNotFound() {
        var category = categoryRepository.findBySlug("non-existent-slug");
        assertThat(category).isEmpty();
    }
}
