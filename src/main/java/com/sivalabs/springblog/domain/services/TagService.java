package com.sivalabs.springblog.domain.services;

import com.sivalabs.springblog.domain.data.TagRepository;
import com.sivalabs.springblog.domain.models.Tag;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional(readOnly = true)
    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    @Transactional
    public Tag getOrCreateTagByName(String name) {
        return tagRepository.getOrCreateTagByName(name);
    }
}
