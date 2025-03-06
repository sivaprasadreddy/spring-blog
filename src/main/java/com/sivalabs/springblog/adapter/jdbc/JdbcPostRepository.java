package com.sivalabs.springblog.adapter.jdbc;

import com.sivalabs.springblog.domain.data.PostRepository;
import com.sivalabs.springblog.domain.models.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcPostRepository implements PostRepository {
    private final JdbcClient jdbcClient;

    public JdbcPostRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void create(Post post) {
        String sql =
                """
                insert into posts (title, slug, short_description, content_markdown,
                                   content_html, status, category_id, created_by)
                values (:title, :slug, :short_description, :content_markdown,
                        :content_html, :status, :category_id, :created_by)
                returning id
                """;
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient
                .sql(sql)
                .param("title", post.getTitle())
                .param("slug", post.getSlug())
                .param("short_description", post.getShortDescription())
                .param("content_markdown", post.getContentMarkdown())
                .param("content_html", post.getContentHtml())
                .param("status", post.getStatus().name())
                .param("category_id", post.getCategory().getId())
                .param("created_by", post.getCreatedBy().getId())
                .update(keyHolder);
        post.setId(keyHolder.getKeyAs(Long.class));
    }

    @Override
    public PagedResult<Post> findAllPosts(int pageNo, int pageSize) {
        String countSql = "SELECT count(*) FROM posts";
        long totalElements = jdbcClient.sql(countSql).query(Long.class).single();
        if (totalElements == 0) {
            return PagedResult.empty();
        }

        int offset = (pageNo - 1) * pageSize;

        String sql =
                """
            SELECT p.*, c.id as category_id, c.name as category_name, c.slug as category_slug,
                   u.id as user_id, u.name as user_name, u.email as user_email, u.role as user_role
            FROM posts p
            JOIN categories c ON c.id = p.category_id
            JOIN users u ON u.id = p.created_by
            ORDER BY created_date DESC LIMIT :size OFFSET :offset
            """;

        var posts = jdbcClient
                .sql(sql)
                .param("size", pageSize)
                .param("offset", offset)
                .query(new PostRowMapper())
                .list();

        return PagedResult.of(posts, pageNo, pageSize, totalElements);
    }

    @Override
    public Optional<Post> findBySlug(String slug) {
        String sql =
                """
            SELECT p.*, c.id as category_id, c.name as category_name, c.slug as category_slug,
                   u.id as user_id, u.name as user_name, u.email as user_email, u.role as user_role
            FROM posts p
            JOIN categories c ON c.id = p.category_id
            JOIN users u ON u.id = p.created_by
            WHERE p.slug = ?
            """;
        return jdbcClient.sql(sql).param(slug).query(new PostRowMapper()).optional();
    }

    static class PostRowMapper implements RowMapper<Post> {
        @Override
        public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
            var category = new Category(
                    rs.getLong("category_id"), rs.getString("category_name"), rs.getString("category_slug"));
            var user = new User(
                    rs.getLong("user_id"),
                    rs.getString("user_email"),
                    null,
                    rs.getString("user_name"),
                    Role.valueOf(rs.getString("user_role")));
            return new Post(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("slug"),
                    rs.getString("short_description"),
                    rs.getString("content_markdown"),
                    rs.getString("content_html"),
                    category,
                    Set.of(),
                    PostStatus.valueOf(rs.getString("status")),
                    user,
                    rs.getTimestamp("created_date").toLocalDateTime());
        }
    }
}
