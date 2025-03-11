package com.sivalabs.springblog.adapter.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.springblog.TestcontainersConfig;
import com.sivalabs.springblog.domain.models.Tag;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Test
    void shouldFindTagsByPostIds() {
        // Call the method with a list of postIds
        Map<Long, Set<Tag>> tagsByPostIds = tagRepository.findTagsByPostIds(List.of(1L, 2L, 3L));

        // Verify the results
        assertThat(tagsByPostIds).hasSize(3);

        // Post 1 should have tags 1 (Java) and 2 (SpringBoot)
        assertThat(tagsByPostIds.get(1L)).hasSize(2);
        assertThat(tagsByPostIds.get(1L)).extracting(Tag::getName).containsExactlyInAnyOrder("Java", "SpringBoot");

        // Post 2 should have tags 2 (SpringBoot) and 3 (Quarkus)
        assertThat(tagsByPostIds.get(2L)).hasSize(2);
        assertThat(tagsByPostIds.get(2L)).extracting(Tag::getName).containsExactlyInAnyOrder("SpringBoot", "Quarkus");

        // Post 3 should have tags 1 (Java) and 3 (Quarkus)
        assertThat(tagsByPostIds.get(3L)).hasSize(2);
        assertThat(tagsByPostIds.get(3L)).extracting(Tag::getName).containsExactlyInAnyOrder("Java", "Quarkus");
    }

    @Test
    void shouldHandleEmptyOrNullListInFindTagsByPostIds() {
        // Test with empty list
        Map<Long, Set<Tag>> emptyResult = tagRepository.findTagsByPostIds(List.of());
        assertThat(emptyResult).isEmpty();

        // Test with null list
        Map<Long, Set<Tag>> nullResult = tagRepository.findTagsByPostIds(null);
        assertThat(nullResult).isEmpty();
    }

    @Test
    void shouldFindTagBySlug() {
        // Find an existing tag by slug
        var tag = tagRepository.findBySlug("java");
        assertThat(tag).isPresent();
        assertThat(tag.get().getName()).isEqualTo("Java");
        assertThat(tag.get().getId()).isEqualTo(1L);
    }

    @Test
    void shouldReturnEmptyWhenTagSlugNotFound() {
        var tag = tagRepository.findBySlug("non-existent-slug");
        assertThat(tag).isEmpty();
    }
}
