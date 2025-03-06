package com.sivalabs.springblog.adapter.jpa;

import com.sivalabs.springblog.TestcontainersConfig;
import com.sivalabs.springblog.domain.models.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import(TestcontainersConfig.class)
@Sql("/test-data.sql")
class JpaUserRepositoryTest {

    @Autowired
    private JpaUserRepository userRepository;

    @Test
    void shouldGetUserByEmail() {
        UserEntity user = userRepository
                .findByEmailIgnoreCase("geovanny.mendoza@example.com")
                .orElseThrow();
        Assertions.assertThat(user.getName()).isEqualTo("Geovanny Mendoza");
        Assertions.assertThat(user.getRole()).isEqualTo(Role.ROLE_USER);
    }

    @Test
    void shouldReturnEmptyWhenUserEmailNotExists() {
        Assertions.assertThat(userRepository.findByEmailIgnoreCase("invalid_user_email"))
                .isEmpty();
    }
}
