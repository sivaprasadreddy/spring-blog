package com.sivalabs.springblog.adapter.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.springblog.TestcontainersConfig;
import com.sivalabs.springblog.domain.models.Tag;
import java.util.List;
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
class JdbcTagRepositoryTest {
    @Autowired
    private JdbcClient jdbcClient;

    private JdbcTagRepository tagRepository;

    @BeforeEach
    void setUp() {
        tagRepository = new JdbcTagRepository(jdbcClient);
    }

    @Test
    void shouldCreateTag() {
        Tag tag = new Tag(null, "Test Tag", "test-tag");
        Tag savedTag = tagRepository.create(tag);
        assertThat(savedTag.getId()).isNotNull();
        assertThat(savedTag.getName()).isEqualTo("Test Tag");
        assertThat(savedTag.getSlug()).isEqualTo("test-tag");
    }

    @Test
    void shouldFindTagById() {
        var tag = tagRepository.findById(1L);
        assertThat(tag).isPresent();
        assertThat(tag.get().getName()).isEqualTo("Java");
        assertThat(tag.get().getSlug()).isEqualTo("java");
    }

    @Test
    void shouldReturnEmptyWhenTagNotFound() {
        var tag = tagRepository.findById(999L);
        assertThat(tag).isEmpty();
    }

    @Test
    void shouldGetAllTags() {
        List<Tag> tags = tagRepository.findAll();
        assertThat(tags).hasSize(10);
    }

    @Test
    void shouldUpdateTag() {
        Tag tag = new Tag(1L, "Updated Java", "updated-java");
        tagRepository.update(tag);

        var updatedTag = tagRepository.findById(1L);
        assertThat(updatedTag).isPresent();
        assertThat(updatedTag.get().getName()).isEqualTo("Updated Java");
        assertThat(updatedTag.get().getSlug()).isEqualTo("updated-java");
    }

    @Test
    void shouldDeleteTag() {
        // Create a new tag first
        Tag newTag = new Tag(null, "Test Delete", "test-delete");
        Tag savedTag = tagRepository.create(newTag);
        Long tagId = savedTag.getId();

        // Delete the tag
        tagRepository.deleteById(tagId);

        // Verify it's deleted
        var tag = tagRepository.findById(tagId);
        assertThat(tag).isEmpty();
    }
}
