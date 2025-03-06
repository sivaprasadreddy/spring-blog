package com.sivalabs.springblog.adapter.jdbc;

import com.sivalabs.springblog.domain.data.UserRepository;
import com.sivalabs.springblog.domain.models.Role;
import com.sivalabs.springblog.domain.models.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserRepository implements UserRepository {
    private final JdbcClient jdbcClient;

    public JdbcUserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE lower(email) = ?";
        return jdbcClient
                .sql(sql)
                .param(email.toLowerCase())
                .query(new UserRowMapper())
                .optional();
    }

    @Override
    public void create(User user) {
        String sql =
                """
        INSERT INTO users (email, password, name, role)
        VALUES (:email, :password, :name, :role)
        RETURNING id
        """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient
                .sql(sql)
                .param("email", user.getEmail())
                .param("password", user.getPassword())
                .param("name", user.getName())
                .param("role", user.getRole().name())
                .update(keyHolder);
        user.setId(keyHolder.getKeyAs(Long.class));
    }

    static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name"),
                    Role.valueOf(rs.getString("role")));
        }
    }
}
