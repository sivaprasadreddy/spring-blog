package com.sivalabs.springblog.adapter.jdbc;

import com.sivalabs.springblog.domain.data.TagRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTagRepository implements TagRepository {
    private final JdbcClient jdbcClient;

    public JdbcTagRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
}
