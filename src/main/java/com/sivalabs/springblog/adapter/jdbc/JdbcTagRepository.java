package com.sivalabs.springblog.adapter.jdbc;

import com.sivalabs.springblog.domain.data.TagRepository;
import com.sivalabs.springblog.domain.models.Tag;
import com.sivalabs.springblog.domain.services.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTagRepository implements TagRepository {
    private final JdbcClient jdbcClient;

    public JdbcTagRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Tag> findAll() {
        String sql = "select * from tags order by name";
        return jdbcClient.sql(sql).query(new TagRowMapper()).list();
    }

    @Override
    public Optional<Tag> findById(Long id) {
        String sql = "select * from tags where id = ?";
        return jdbcClient.sql(sql).param(id).query(new TagRowMapper()).optional();
    }

    @Override
    public Optional<Tag> findBySlug(String slug) {
        String sql = "select * from tags where slug = ?";
        return jdbcClient.sql(sql).param(slug).query(new TagRowMapper()).optional();
    }

    @Override
    public Tag create(Tag tag) {
        String slug = tag.getSlug();
        if (slug == null) {
            slug = StringUtils.toSlug(tag.getName());
            tag.setSlug(slug);
        }
        String sql =
                """
                insert into tags (name, slug)
                values (:name, :slug)
                returning id
                """;
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient
                .sql(sql)
                .param("name", tag.getName())
                .param("slug", tag.getSlug())
                .update(keyHolder);
        tag.setId(keyHolder.getKeyAs(Long.class));
        return tag;
    }

    @Override
    public Tag getOrCreateTagByName(String name) {
        String slug = StringUtils.toSlug(name);
        Tag tag = findBySlug(slug).orElse(null);
        if (tag == null) {
            tag = new Tag(null, name, slug);
            tag = create(tag);
        }
        return tag;
    }

    @Override
    public void update(Tag tag) {
        String sql =
                """
                update tags
                set name = :name,
                    slug = :slug
                where id = :id
                """;
        jdbcClient
                .sql(sql)
                .param("name", tag.getName())
                .param("slug", tag.getSlug())
                .param("id", tag.getId())
                .update();
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from tags where id = ?";
        jdbcClient.sql(sql).param(id).update();
    }

    @Override
    public Map<Long, Set<Tag>> findTagsByPostIds(List<Long> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return Map.of();
        }

        String sql =
                """
            SELECT t.*, pt.post_id FROM tags t
            JOIN post_tags pt ON t.id = pt.tag_id
            WHERE pt.post_id IN (:postIds)
            ORDER BY t.name
            """;

        List<TagWithPostId> tagsWithPostIds = jdbcClient
                .sql(sql)
                .param("postIds", postIds)
                .query((rs, rowNum) -> new TagWithPostId(
                        rs.getLong("id"), rs.getString("name"), rs.getString("slug"), rs.getLong("post_id")))
                .list();

        Map<Long, Set<Tag>> result = new HashMap<>();
        for (TagWithPostId tagWithPostId : tagsWithPostIds) {
            result.computeIfAbsent(tagWithPostId.postId(), k -> new HashSet<>())
                    .add(new Tag(tagWithPostId.id(), tagWithPostId.name(), tagWithPostId.slug()));
        }

        return result;
    }

    record TagWithPostId(Long id, String name, String slug, Long postId) {}

    static class TagRowMapper implements RowMapper<Tag> {
        @Override
        public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Tag(rs.getLong("id"), rs.getString("name"), rs.getString("slug"));
        }
    }
}
