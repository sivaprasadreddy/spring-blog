package com.sivalabs.springblog.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(
        properties = {
            "spring.test.database.replace=none",
            "spring.datasource.url=jdbc:tc:postgresql:17:///db",
        })
@Sql("/test-data.sql")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldGetAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        assertThat(users).hasSize(2);
    }

    @Test
    void shouldGetUserByEmail() {
        UserEntity user =
                userRepository.findByEmail("geovanny.mendoza@example.com").orElseThrow();
        assertThat(user.getName()).isEqualTo("Geovanny Mendoza");
        assertThat(user.getPassword()).isEqualTo("password456");
        assertThat(user.getRole()).isEqualTo("User");
    }

    @Test
    void shouldReturnEmptyWhenUserEmailNotExists() {
        assertThat(userRepository.findByEmail("invalid_user_email")).isEmpty();
    }
}
