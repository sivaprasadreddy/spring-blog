package com.sivalabs.springblog.adapter.jdbc;

import com.sivalabs.springblog.domain.data.CommentRepository;
import com.sivalabs.springblog.domain.models.Comment;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcCommentRepository implements CommentRepository {
    private final JdbcClient jdbcClient;

    public JdbcCommentRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Comment create(Comment comment) {
        String sql =
                """
                insert into comments (name, content, post_id, created_by, created_date)
                values (:name, :content, :post_id, :created_by, :created_date)
                returning id
                """;
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient
                .sql(sql)
                .param("name", comment.getName())
                .param("content", comment.getContent())
                .param("post_id", comment.getPostId())
                .param("created_by", comment.getCreatedUserId())
                .param("created_date", comment.getCreatedDate())
                .update(keyHolder);
        comment.setId(keyHolder.getKeyAs(Long.class));
        return comment;
    }

    @Override
    public Optional<Comment> findById(Long id) {
        String sql = "select * from comments where id = ?";
        return jdbcClient.sql(sql).param(id).query(new CommentRowMapper()).optional();
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        String sql = "select * from comments where post_id = ? order by created_date desc";
        return jdbcClient.sql(sql).param(postId).query(new CommentRowMapper()).list();
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from comments where id = ?";
        jdbcClient.sql(sql).param(id).update();
    }

    static class CommentRowMapper implements RowMapper<Comment> {
        @Override
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Comment(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("content"),
                    rs.getLong("post_id"),
                    rs.getLong("created_by"),
                    rs.getTimestamp("created_date").toLocalDateTime());
        }
    }
}
