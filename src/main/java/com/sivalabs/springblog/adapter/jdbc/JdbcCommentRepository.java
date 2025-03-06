package com.sivalabs.springblog.adapter.jdbc;

import com.sivalabs.springblog.domain.data.CommentRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcCommentRepository implements CommentRepository {
    private final JdbcClient jdbcClient;

    public JdbcCommentRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
}
