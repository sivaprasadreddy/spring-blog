package com.sivalabs.springblog.adapter.jdbc;

import com.sivalabs.springblog.domain.data.TagRepository;
import com.sivalabs.springblog.domain.models.Tag;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
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
    public Tag create(Tag tag) {
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
    public Optional<Tag> findById(Long id) {
        String sql = "select * from tags where id = ?";
        return jdbcClient.sql(sql).param(id).query(new TagRowMapper()).optional();
    }

    @Override
    public List<Tag> findAll() {
        String sql = "select * from tags order by name";
        return jdbcClient.sql(sql).query(new TagRowMapper()).list();
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

    static class TagRowMapper implements RowMapper<Tag> {
        @Override
        public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Tag(rs.getLong("id"), rs.getString("name"), rs.getString("slug"));
        }
    }
}
