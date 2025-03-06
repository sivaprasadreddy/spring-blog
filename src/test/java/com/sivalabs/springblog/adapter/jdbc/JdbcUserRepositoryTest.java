package com.sivalabs.springblog.adapter.jdbc;

import com.sivalabs.springblog.TestcontainersConfig;
import com.sivalabs.springblog.domain.models.Role;
import com.sivalabs.springblog.domain.models.User;
import org.assertj.core.api.Assertions;
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
class JdbcUserRepositoryTest {

    @Autowired
    private JdbcClient jdbcClient;

    private JdbcUserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new JdbcUserRepository(jdbcClient);
    }

    @Test
    void shouldGetUserByEmail() {
        User user = userRepository.findByEmail("geovanny.mendoza@example.com").orElseThrow();
        Assertions.assertThat(user.getName()).isEqualTo("Geovanny Mendoza");
        Assertions.assertThat(user.getRole()).isEqualTo(Role.ROLE_USER);
    }

    @Test
    void shouldReturnEmptyWhenUserEmailNotExists() {
        Assertions.assertThat(userRepository.findByEmail("invalid_user_email")).isEmpty();
    }
}
