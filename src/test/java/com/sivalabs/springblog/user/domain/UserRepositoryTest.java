package com.sivalabs.springblog.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.springblog.TestcontainersConfig;
import com.sivalabs.springblog.user.domain.models.RoleEnum;
import com.sivalabs.springblog.user.domain.models.UserEntity;
import com.sivalabs.springblog.user.domain.repositories.UserRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import(TestcontainersConfig.class)
@Sql("/test-data.sql")
class UserRepositoryTest {

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
        assertThat(user.getRole()).isEqualTo(RoleEnum.USER);
    }

    @Test
    void shouldReturnEmptyWhenUserEmailNotExists() {
        assertThat(userRepository.findByEmail("invalid_user_email")).isEmpty();
    }
}
