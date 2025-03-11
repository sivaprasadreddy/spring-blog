package com.sivalabs.springblog.adapter.jdbc;

import com.sivalabs.springblog.domain.data.CommentRepository;
import com.sivalabs.springblog.domain.models.Comment;
import com.sivalabs.springblog.domain.models.Role;
import com.sivalabs.springblog.domain.models.User;
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
    public List<Comment> findAll() {
        String sql =
                """
        select c.*, u.id as user_id, u.name as user_name, u.email as user_email, u.role as user_role
        from comments c join users u on c.created_by = u.id
        order by c.created_date desc
        """;
        return jdbcClient.sql(sql).query(new CommentRowMapper()).list();
    }

    @Override
    public Optional<Comment> findById(Long id) {
        String sql =
                """
        select c.*, u.id as user_id, u.name as user_name, u.email as user_email, u.role as user_role
        from comments c join users u on c.created_by = u.id
        where c.id = ?
        """;
        return jdbcClient.sql(sql).param(id).query(new CommentRowMapper()).optional();
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        String sql =
                """
        select c.*, u.id as user_id, u.name as user_name, u.email as user_email, u.role as user_role
        from comments c join users u on c.created_by = u.id
        where c.post_id = ? order by c.created_date
        """;
        return jdbcClient.sql(sql).param(postId).query(new CommentRowMapper()).list();
    }

    @Override
    public Comment create(Comment comment) {
        String sql =
                """
                insert into comments (content, post_id, created_by, created_date)
                values (:content, :post_id, :created_by, :created_date)
                returning id
                """;
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient
                .sql(sql)
                .param("content", comment.getContent())
                .param("post_id", comment.getPostId())
                .param("created_by", comment.getCreatedBy().getId())
                .param("created_date", comment.getCreatedDate())
                .update(keyHolder);
        comment.setId(keyHolder.getKeyAs(Long.class));
        return comment;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from comments where id = ?";
        jdbcClient.sql(sql).param(id).update();
    }

    @Override
    public void deleteCommentsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        String placeholders = String.join(",", ids.stream().map(id -> "?").toList());
        String sql = "delete from comments where id IN (" + placeholders + ")";

        var spec = jdbcClient.sql(sql);
        for (Long id : ids) {
            spec = spec.param(id);
        }
        spec.update();
    }

    @Override
    public void deleteCommentsByPostIds(List<Long> ids) {
        String sql = "delete from comments where post_id IN (:postIds)";
        jdbcClient.sql(sql).param("postIds", ids).update();
    }

    static class CommentRowMapper implements RowMapper<Comment> {
        @Override
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            var createdBy = new User(
                    rs.getLong("user_id"),
                    rs.getString("user_email"),
                    null,
                    rs.getString("user_name"),
                    Role.valueOf(rs.getString("user_role")));
            return new Comment(
                    rs.getLong("id"),
                    rs.getString("content"),
                    rs.getLong("post_id"),
                    createdBy,
                    rs.getTimestamp("created_date").toLocalDateTime());
        }
    }
}
