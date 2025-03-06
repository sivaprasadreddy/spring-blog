package com.sivalabs.springblog.adapter.jdbc;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.springblog.TestcontainersConfig;
import com.sivalabs.springblog.domain.models.*;
import java.time.LocalDateTime;
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
class JdbcPostRepositoryTest {
    @Autowired
    private JdbcClient jdbcClient;

    private JdbcPostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository = new JdbcPostRepository(jdbcClient);
    }

    @Test
    void shouldGetPosts() {
        PagedResult<Post> pagedResult = postRepository.findAllPosts(1, 10);
        assertThat(pagedResult.totalElements()).isEqualTo(22);
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

        postRepository.create(post);

        assertThat(post.getId()).isGreaterThan(0);
        System.out.println("postId: " + post.getId());
    }
}
