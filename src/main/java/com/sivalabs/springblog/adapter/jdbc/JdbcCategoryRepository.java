package com.sivalabs.springblog.adapter.jdbc;

import com.sivalabs.springblog.domain.data.CategoryRepository;
import com.sivalabs.springblog.domain.models.Category;
import com.sivalabs.springblog.domain.services.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcCategoryRepository implements CategoryRepository {
    private final JdbcClient jdbcClient;

    public JdbcCategoryRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Category> findAll() {
        String sql = "select * from categories order by name";
        return jdbcClient.sql(sql).query(new CategoryRowMapper()).list();
    }

    @Override
    public Optional<Category> findById(Long id) {
        String sql = "select * from categories where id = ?";
        return jdbcClient.sql(sql).param(id).query(new CategoryRowMapper()).optional();
    }

    @Override
    public Optional<Category> findBySlug(String slug) {
        String sql = "select * from categories where slug = ?";
        return jdbcClient.sql(sql).param(slug).query(new CategoryRowMapper()).optional();
    }

    @Override
    public Category create(Category category) {
        String sql =
                """
                insert into categories (name, slug)
                values (:name, :slug)
                returning id
                """;
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient
                .sql(sql)
                .param("name", category.getName())
                .param("slug", category.getSlug())
                .update(keyHolder);
        category.setId(keyHolder.getKeyAs(Long.class));
        return category;
    }

    @Override
    public Category getOrCreateCategoryByName(String name) {
        String slug = StringUtils.toSlug(name);
        Category category = findBySlug(slug).orElse(null);
        if (category == null) {
            category = new Category(null, name, slug);
            category = create(category);
        }
        return category;
    }

    @Override
    public void update(Category category) {
        String sql =
                """
                update categories
                set name = :name,
                    slug = :slug
                where id = :id
                """;
        jdbcClient
                .sql(sql)
                .param("name", category.getName())
                .param("slug", category.getSlug())
                .param("id", category.getId())
                .update();
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from categories where id = ?";
        jdbcClient.sql(sql).param(id).update();
    }

    static class CategoryRowMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Category(rs.getLong("id"), rs.getString("name"), rs.getString("slug"));
        }
    }
}
