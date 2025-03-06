package com.sivalabs.springblog.domain.services;

import com.sivalabs.springblog.domain.data.PostRepository;
import com.sivalabs.springblog.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.springblog.domain.models.PagedResult;
import com.sivalabs.springblog.domain.models.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PagedResult<Post> getPosts(int pageNo, int pageSize) {
        return postRepository.findAllPosts(pageNo, pageSize);
    }

    public Post getPostBySlug(String slug) {
        return postRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with slug: " + slug));
    }
}
