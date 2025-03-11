package com.sivalabs.springblog.domain.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.springblog.TestcontainersConfig;
import com.sivalabs.springblog.domain.data.TagRepository;
import com.sivalabs.springblog.domain.models.Tag;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Import(TestcontainersConfig.class)
@Sql("/test-data.sql")
class TagServiceTest {

    @Autowired
    TagService tagService;

    @Autowired
    TagRepository tagRepository;

    @Test
    void shouldGetAllTags() {
        List<Tag> tags = tagService.findAllTags();
        assertThat(tags).hasSize(10);
    }

    @Test
    void shouldGetExistingTagByName() {
        // Test with an existing tag
        Tag existingTag = tagService.getOrCreateTagByName("Java");
        assertThat(existingTag).isNotNull();
        assertThat(existingTag.getId()).isEqualTo(1L);
        assertThat(existingTag.getName()).isEqualTo("Java");
        assertThat(existingTag.getSlug()).isEqualTo("java");
    }

    @Test
    void shouldCreateNewTagByName() {
        // Test with a new tag name
        String newTagName = "New Test Tag";
        Tag newTag = tagService.getOrCreateTagByName(newTagName);

        assertThat(newTag).isNotNull();
        assertThat(newTag.getId()).isNotNull();
        assertThat(newTag.getName()).isEqualTo(newTagName);
        assertThat(newTag.getSlug()).isEqualTo("new-test-tag");

        // Verify the tag was actually created in the database
        var savedTag = tagRepository.findBySlug("new-test-tag");
        assertThat(savedTag).isPresent();
        assertThat(savedTag.get().getName()).isEqualTo(newTagName);
    }
}
