package com.sivalabs.springblog.domain.data;

import com.sivalabs.springblog.domain.models.Tag;
import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Tag create(Tag tag);

    Optional<Tag> findById(Long id);

    List<Tag> findAll();

    void update(Tag tag);

    void deleteById(Long id);
}
