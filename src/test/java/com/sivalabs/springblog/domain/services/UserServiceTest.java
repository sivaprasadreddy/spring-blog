package com.sivalabs.springblog.domain.services;

import com.sivalabs.springblog.TestcontainersConfig;
import com.sivalabs.springblog.domain.models.CreateUserCmd;
import com.sivalabs.springblog.domain.models.Role;
import com.sivalabs.springblog.domain.models.User;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Import(TestcontainersConfig.class)
@Sql("/test-data.sql")
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void shouldGetUserByEmail() {
        User user = userService.findByEmail("siva@gmail.com").orElseThrow();
        Assertions.assertThat(user.getName()).isEqualTo("Siva Prasad");
        Assertions.assertThat(user.getRole()).isEqualTo(Role.ROLE_ADMIN);
    }

    @Test
    void shouldReturnEmptyWhenUserEmailNotExists() {
        Assertions.assertThat(userService.findByEmail("invalid_user_email")).isEmpty();
    }

    @Test
    void shouldCreateUser() {
        // Create a new user
        var cmd = new CreateUserCmd("Test User", "test.user@example.com", "password123", Role.ROLE_USER);

        // Save the user
        userService.createUser(cmd);

        // Verify the user can be retrieved from the database
        Optional<User> retrievedUser = userService.findByEmail("test.user@example.com");
        Assertions.assertThat(retrievedUser).isPresent();
        Assertions.assertThat(retrievedUser.get().getId()).isNotNull();
        Assertions.assertThat(retrievedUser.get().getEmail()).isEqualTo("test.user@example.com");
        boolean pwdMatches =
                passwordEncoder.matches("password123", retrievedUser.get().getPassword());
        Assertions.assertThat(pwdMatches).isTrue();
        Assertions.assertThat(retrievedUser.get().getName()).isEqualTo("Test User");
        Assertions.assertThat(retrievedUser.get().getRole()).isEqualTo(Role.ROLE_USER);
    }
}
