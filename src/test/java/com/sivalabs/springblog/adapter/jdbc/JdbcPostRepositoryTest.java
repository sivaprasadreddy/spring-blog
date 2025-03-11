package com.sivalabs.springblog.adapter.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.springblog.TestcontainersConfig;
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
    void shouldFindPostsCount() {
        Long count = postRepository.findPostsCount();
        assertThat(count).isEqualTo(22); // Based on test-data.sql
    }
}
