package com.sivalabs.springblog.domain.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.springblog.TestcontainersConfig;
import com.sivalabs.springblog.domain.data.CategoryRepository;
import com.sivalabs.springblog.domain.models.Category;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Import(TestcontainersConfig.class)
@Sql("/test-data.sql")
class CategoryServiceTest {
    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void shouldGetAllCategories() {
        List<Category> categories = categoryService.findAllCategories();
        assertThat(categories).hasSize(10);
    }

    @Test
    void shouldGetExistingCategoryByName() {
        Category category = categoryService.getOrCreateCategoryByName("Java");
        assertThat(category).isNotNull();
        assertThat(category.getId()).isEqualTo(1L);
        assertThat(category.getName()).isEqualTo("Java");
        assertThat(category.getSlug()).isEqualTo("java");
    }

    @Test
    void shouldCreateNewCategoryWhenNotExistsByName() {
        String newCategoryName = "New Test Category";
        Category category = categoryService.getOrCreateCategoryByName(newCategoryName);

        assertThat(category).isNotNull();
        assertThat(category.getId()).isNotNull();
        assertThat(category.getName()).isEqualTo(newCategoryName);
        assertThat(category.getSlug()).isEqualTo("new-test-category");

        // Verify it was actually saved to the database
        var savedCategory = categoryRepository.findById(category.getId());
        assertThat(savedCategory).isPresent();
        assertThat(savedCategory.get().getName()).isEqualTo(newCategoryName);
    }
}
