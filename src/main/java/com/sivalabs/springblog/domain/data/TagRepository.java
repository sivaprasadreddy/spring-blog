package com.sivalabs.springblog.domain.data;

import com.sivalabs.springblog.domain.models.Tag;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface TagRepository {

    List<Tag> findAll();

    Optional<Tag> findById(Long id);

    Optional<Tag> findBySlug(String slug);

    Map<Long, Set<Tag>> findTagsByPostIds(List<Long> postIds);

    Tag create(Tag tag);

    Tag getOrCreateTagByName(String name);

    void update(Tag tag);

    void deleteById(Long id);
}
