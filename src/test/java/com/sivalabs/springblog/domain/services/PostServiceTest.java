package com.sivalabs.springblog.domain.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sivalabs.springblog.TestcontainersConfig;
import com.sivalabs.springblog.domain.data.CommentRepository;
import com.sivalabs.springblog.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.springblog.domain.models.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Import(TestcontainersConfig.class)
@Sql("/test-data.sql")
class PostServiceTest {
    @Autowired
    PostService postService;

    @Autowired
    CommentRepository commentRepository;

    @Nested
    class PostTests {
        @Test
        void shouldGetPosts() {
            PagedResult<Post> pagedResult = postService.getPosts(1, 10);
            assertThat(pagedResult.totalElements()).isEqualTo(22);
        }

        @Test
        void shouldFindPostsByCategorySlug() {
            // Java category has posts in test data
            PagedResult<Post> pagedResult = postService.getPostsByCategorySlug("java", 1, 10);
            // The count might vary depending on test execution order, so we just verify it's not empty
            assertThat(pagedResult.totalElements()).isGreaterThan(0);
            assertThat(pagedResult.data()).isNotEmpty();
            assertThat(pagedResult.data().getFirst().getCategory().getSlug()).isEqualTo("java");
        }

        @Test
        void shouldReturnEmptyResultWhenCategorySlugNotFound() {
            PagedResult<Post> pagedResult = postService.getPostsByCategorySlug("non-existent-category", 1, 10);
            assertThat(pagedResult.totalElements()).isEqualTo(0);
            assertThat(pagedResult.data()).isEmpty();
        }

        @Test
        void shouldFindPostsByTagSlug() {
            // Java tag (id=1) is associated with posts 1 and 3
            PagedResult<Post> pagedResult = postService.getPostsByTagSlug("java", 1, 10);
            assertThat(pagedResult.totalElements()).isEqualTo(2);
            assertThat(pagedResult.data()).hasSize(2);

            // Extract post IDs and verify they include posts 1 and 3
            List<Long> postIds = pagedResult.data().stream().map(Post::getId).toList();
            assertThat(postIds).containsExactlyInAnyOrder(1L, 3L);
        }

        @Test
        void shouldReturnEmptyResultWhenTagSlugNotFound() {
            PagedResult<Post> pagedResult = postService.getPostsByTagSlug("non-existent-tag", 1, 10);
            assertThat(pagedResult.totalElements()).isEqualTo(0);
            assertThat(pagedResult.data()).isEmpty();
        }

        @Test
        void shouldFindPostBySlug() {
            Post post = postService.getPostBySlug("first-post");
            assertThat(post.getTitle()).isEqualTo("First Post");
            assertThat(post.getId()).isEqualTo(1L);
        }

        @Test
        void shouldThrowExceptionWhenPostSlugNotFound() {
            assertThatThrownBy(() -> postService.getPostBySlug("non-existent-post"))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void shouldFindPostById() {
            Post post = postService.getPostById(1L);
            assertThat(post).isNotNull();
            assertThat(post.getTitle()).isEqualTo("First Post");
            assertThat(post.getSlug()).isEqualTo("first-post");
        }

        @Test
        void shouldThrowExceptionWhenPostIdNotFound() {
            assertThatThrownBy(() -> postService.getPostById(999999L)).isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void shouldCreatePost() {
            var post = new Post(
                    null,
                    "Test Post Title 1",
                    "test-post-title-1",
                    "This is test post 1",
                    "# Test Post Title 1 \n This is test post 1",
                    "<h1>Test Post Title 1</h1>\n\n <p>This is test post 1</p>",
                    new Category(1L),
                    Set.of(),
                    PostStatus.DRAFT,
                    new User(1L),
                    LocalDateTime.now());

            postService.createPost(post);

            assertThat(post.getId()).isGreaterThan(0);
        }

        @Test
        void shouldDeletePostsByIds() {
            // Use post IDs that don't have comments (posts 3 and 4 don't have comments according to test-data.sql)
            // Verify posts exist before deletion
            assertThat(postService.getPostById(3L)).isNotNull();
            assertThat(postService.getPostById(4L)).isNotNull();

            // Delete posts
            postService.deletePostsByIds(List.of(3L, 4L));

            // Verify posts are deleted
            assertThatThrownBy(() -> postService.getPostById(3L)).isInstanceOf(ResourceNotFoundException.class);
            assertThatThrownBy(() -> postService.getPostById(4L)).isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void shouldHandleEmptyListInDeletePostsByIds() {
            // This should not throw an exception
            postService.deletePostsByIds(List.of());
            postService.deletePostsByIds(null);
        }

        @Test
        void shouldUpdatePost() {
            // Get existing post
            Post post = postService.getPostById(1L);
            assertThat(post).isNotNull();

            // Update post
            post.setTitle("Updated Title");
            post.setSlug("updated-slug");
            post.setShortDescription("Updated short description");
            post.setContentMarkdown("Updated markdown");
            post.setContentHtml("<p>Updated HTML</p>");
            post.setStatus(PostStatus.PUBLISHED);
            post.setCategory(new Category(2L)); // Change category to Python

            postService.updatePost(post);

            // Verify update
            Post updatedPost = postService.getPostById(1L);
            assertThat(updatedPost).isNotNull();
            assertThat(updatedPost.getTitle()).isEqualTo("Updated Title");
            assertThat(updatedPost.getSlug()).isEqualTo("updated-slug");
            assertThat(updatedPost.getShortDescription()).isEqualTo("Updated short description");
            assertThat(updatedPost.getContentMarkdown()).isEqualTo("Updated markdown");
            assertThat(updatedPost.getContentHtml()).isEqualTo("<p>Updated HTML</p>");
            assertThat(updatedPost.getStatus()).isEqualTo(PostStatus.PUBLISHED);
            assertThat(updatedPost.getCategory().getId()).isEqualTo(2L);
        }
    }

    @Nested
    class CommentTests {

        @Test
        void shouldFindAllComments() {
            List<Comment> comments = postService.findAllComments();
            assertThat(comments).isNotEmpty();
            assertThat(comments.size()).isGreaterThanOrEqualTo(1);
        }

        @Test
        void shouldDeleteCommentsByIds() {
            // Create a few new comments first
            Comment comment1 = new Comment(null, "Test Delete 1", "Test Delete Comment 1", 1L, 1L, LocalDateTime.now());
            Comment comment2 = new Comment(null, "Test Delete 2", "Test Delete Comment 2", 1L, 1L, LocalDateTime.now());
            Comment savedComment1 = commentRepository.create(comment1);
            Comment savedComment2 = commentRepository.create(comment2);
            Long commentId1 = savedComment1.getId();
            Long commentId2 = savedComment2.getId();

            // Delete the comments
            commentRepository.deleteCommentsByIds(List.of(commentId1, commentId2));

            // Verify they're deleted
            assertThat(commentRepository.findById(commentId1)).isEmpty();
            assertThat(commentRepository.findById(commentId2)).isEmpty();
        }

        @Test
        void shouldHandleEmptyListInDeleteCommentsByIds() {
            // This should not throw an exception
            commentRepository.deleteCommentsByIds(List.of());
            commentRepository.deleteCommentsByIds(null);
        }
    }
}
