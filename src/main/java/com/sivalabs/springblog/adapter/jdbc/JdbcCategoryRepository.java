package com.sivalabs.springblog.adapter.jdbc;

import com.sivalabs.springblog.domain.data.CategoryRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcCategoryRepository implements CategoryRepository {
    private final JdbcClient jdbcClient;

    public JdbcCategoryRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
}
