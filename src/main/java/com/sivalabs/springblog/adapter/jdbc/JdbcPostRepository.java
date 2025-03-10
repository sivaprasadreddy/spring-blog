package com.sivalabs.springblog.adapter.jdbc;

import com.sivalabs.springblog.domain.data.PostRepository;
import com.sivalabs.springblog.domain.models.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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

    public Long create(Post post) {
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
        return post.getId();
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
    public PagedResult<Post> findPostsByCategorySlug(String categorySlug, int pageNo, int pageSize) {
        String countSql = "SELECT count(*) FROM posts p JOIN categories c ON c.id = p.category_id WHERE c.slug = ?";
        long totalElements =
                jdbcClient.sql(countSql).param(categorySlug).query(Long.class).single();
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
            WHERE c.slug = ?
            ORDER BY created_date DESC LIMIT ? OFFSET ?
            """;

        var posts = jdbcClient
                .sql(sql)
                .param(categorySlug)
                .param(pageSize)
                .param(offset)
                .query(new PostRowMapper())
                .list();

        return PagedResult.of(posts, pageNo, pageSize, totalElements);
    }

    @Override
    public PagedResult<Post> findPostsByTagSlug(String tagSlug, int pageNo, int pageSize) {
        String countSql =
                """
            SELECT count(DISTINCT p.id)
            FROM posts p
            JOIN post_tags pt ON p.id = pt.post_id
            JOIN tags t ON t.id = pt.tag_id
            WHERE t.slug = ?
            """;
        long totalElements =
                jdbcClient.sql(countSql).param(tagSlug).query(Long.class).single();
        if (totalElements == 0) {
            return PagedResult.empty();
        }

        int offset = (pageNo - 1) * pageSize;

        String sql =
                """
            SELECT DISTINCT p.*, c.id as category_id, c.name as category_name, c.slug as category_slug,
                   u.id as user_id, u.name as user_name, u.email as user_email, u.role as user_role
            FROM posts p
            JOIN categories c ON c.id = p.category_id
            JOIN users u ON u.id = p.created_by
            JOIN post_tags pt ON p.id = pt.post_id
            JOIN tags t ON t.id = pt.tag_id
            WHERE t.slug = ?
            ORDER BY p.created_date DESC LIMIT ? OFFSET ?
            """;

        var posts = jdbcClient
                .sql(sql)
                .param(tagSlug)
                .param(pageSize)
                .param(offset)
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

    @Override
    public void deletePostsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        // Create placeholders for the IN clause (?, ?, ?)
        String placeholders = String.join(",", ids.stream().map(id -> "?").toList());
        String sql = "DELETE FROM posts WHERE id IN (" + placeholders + ")";

        var spec = jdbcClient.sql(sql);
        for (Long id : ids) {
            spec = spec.param(id);
        }
        spec.update();
    }

    @Override
    public Optional<Post> findById(Long id) {
        String sql =
                """
            SELECT p.*, c.id as category_id, c.name as category_name, c.slug as category_slug,
                   u.id as user_id, u.name as user_name, u.email as user_email, u.role as user_role
            FROM posts p
            JOIN categories c ON c.id = p.category_id
            JOIN users u ON u.id = p.created_by
            WHERE p.id = ?
            """;
        return jdbcClient.sql(sql).param(id).query(new PostRowMapper()).optional();
    }

    @Override
    public void update(Post post) {
        String sql =
                """
                update posts set title = :title, slug = :slug, short_description = :short_description,
                content_markdown = :content_markdown, content_html = :content_html,
                status = :status, category_id = :category_id
                where id = :id
                """;
        jdbcClient
                .sql(sql)
                .param("title", post.getTitle())
                .param("slug", post.getSlug())
                .param("short_description", post.getShortDescription())
                .param("content_markdown", post.getContentMarkdown())
                .param("content_html", post.getContentHtml())
                .param("status", post.getStatus().name())
                .param("category_id", post.getCategory().getId())
                .param("id", post.getId())
                .update();
    }

    @Override
    public void addPostTags(Long postId, Set<Tag> tags) {
        String sql =
                """
                insert into post_tags (post_id, tag_id)
                values (:post_id, :tag_id)
                """;
        tags.forEach(tag -> jdbcClient
                .sql(sql)
                .param("post_id", postId)
                .param("tag_id", tag.getId())
                .update());
    }

    @Override
    public Long findPostsCount() {
        return jdbcClient.sql("SELECT count(*) FROM posts").query(Long.class).single();
    }

    static class PostRowMapper implements RowMapper<Post> {
        @Override
        public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long postId = rs.getLong("id");
            var category = new Category(
                    rs.getLong("category_id"), rs.getString("category_name"), rs.getString("category_slug"));
            var user = new User(
                    rs.getLong("user_id"),
                    rs.getString("user_email"),
                    null,
                    rs.getString("user_name"),
                    Role.valueOf(rs.getString("user_role")));
            return new Post(
                    postId,
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
