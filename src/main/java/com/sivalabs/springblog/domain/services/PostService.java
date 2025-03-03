package com.sivalabs.springblog.domain.services;

import com.sivalabs.springblog.ApplicationProperties;
import com.sivalabs.springblog.domain.entities.PostEntity;
import com.sivalabs.springblog.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.springblog.domain.models.PagedResult;
import com.sivalabs.springblog.domain.repositories.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final ApplicationProperties properties;

    public PostService(PostRepository postRepository, ApplicationProperties properties) {
        this.postRepository = postRepository;
        this.properties = properties;
    }

    public PagedResult<PostEntity> getPosts(int pageNo) {
        Sort sort = Sort.by("createdDate").descending();
        pageNo = pageNo <= 1 ? 0 : pageNo - 1;
        Pageable pageable = PageRequest.of(pageNo, properties.pageSize(), sort);
        Page<PostEntity> productsPage = postRepository.findAllPosts(pageable);
        return PagedResult.from(productsPage);
    }

    public PostEntity getPostBySlug(String slug) {
        return postRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with slug: " + slug));
    }
}
