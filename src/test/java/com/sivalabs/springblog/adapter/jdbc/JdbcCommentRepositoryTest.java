package com.sivalabs.springblog.adapter.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.springblog.TestcontainersConfig;
import com.sivalabs.springblog.domain.models.Comment;
import java.time.LocalDateTime;
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
class JdbcCommentRepositoryTest {
    @Autowired
    private JdbcClient jdbcClient;

    private JdbcCommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        commentRepository = new JdbcCommentRepository(jdbcClient);
    }

    @Test
    void shouldCreateComment() {
        Comment comment = new Comment(null, "Test User", "Test Comment", 1L, 1L, LocalDateTime.now());
        Comment savedComment = commentRepository.create(comment);
        assertThat(savedComment.getId()).isNotNull();
        assertThat(savedComment.getName()).isEqualTo("Test User");
        assertThat(savedComment.getContent()).isEqualTo("Test Comment");
        assertThat(savedComment.getPostId()).isEqualTo(1L);
        assertThat(savedComment.getCreatedUserId()).isEqualTo(1L);
        assertThat(savedComment.getCreatedDate()).isNotNull();
    }

    @Test
    void shouldFindCommentById() {
        var comment = commentRepository.findById(1L);
        assertThat(comment).isPresent();
        assertThat(comment.get().getName()).isEqualTo("Geovanny");
        assertThat(comment.get().getContent()).isEqualTo("This is a comment on the first post.");
    }

    @Test
    void shouldReturnEmptyWhenCommentNotFound() {
        var comment = commentRepository.findById(999L);
        assertThat(comment).isEmpty();
    }

    @Test
    void shouldFindCommentsByPostId() {
        List<Comment> comments = commentRepository.findByPostId(1L);
        assertThat(comments).isNotEmpty();
        assertThat(comments.get(0).getPostId()).isEqualTo(1L);
    }

    @Test
    void shouldDeleteComment() {
        // Create a new comment first
        Comment newComment = new Comment(null, "Test Delete", "Test Delete Comment", 1L, 1L, LocalDateTime.now());
        Comment savedComment = commentRepository.create(newComment);
        Long commentId = savedComment.getId();

        // Delete the comment
        commentRepository.deleteById(commentId);

        // Verify it's deleted
        var comment = commentRepository.findById(commentId);
        assertThat(comment).isEmpty();
    }

    @Test
    void shouldDeleteCommentsByPostIds() {
        // Create a few new comments for a specific post
        Long postId = 2L;
        Comment comment1 =
                new Comment(null, "Test Delete Post 1", "Test Delete Post Comment 1", postId, 1L, LocalDateTime.now());
        Comment comment2 =
                new Comment(null, "Test Delete Post 2", "Test Delete Post Comment 2", postId, 1L, LocalDateTime.now());
        commentRepository.create(comment1);
        commentRepository.create(comment2);

        // Verify comments exist for this post
        List<Comment> commentsBeforeDelete = commentRepository.findByPostId(postId);
        assertThat(commentsBeforeDelete).isNotEmpty();

        // Delete comments for this post
        commentRepository.deleteCommentsByPostIds(List.of(postId));

        // Verify they're deleted
        List<Comment> commentsAfterDelete = commentRepository.findByPostId(postId);
        assertThat(commentsAfterDelete).isEmpty();
    }
}
