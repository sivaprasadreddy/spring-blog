package com.sivalabs.springblog.adapter.jpa;

import com.sivalabs.springblog.domain.data.PostRepository;
import com.sivalabs.springblog.domain.models.PagedResult;
import com.sivalabs.springblog.domain.models.Post;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
class PostRepositoryJpaImpl implements PostRepository {
    private final JpaPostRepository jpaPostRepo;
    private final JpaEntityMapper entityMapper;

    public PostRepositoryJpaImpl(JpaPostRepository jpaPostRepository, JpaEntityMapper entityMapper) {
        this.jpaPostRepo = jpaPostRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public PagedResult<Post> findAllPosts(int pageNo, int pageSize) {
        Sort sort = Sort.by("createdDate").descending();
        pageNo = pageNo <= 1 ? 0 : pageNo - 1;
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<PostEntity> postEntityPage = jpaPostRepo.findAllPosts(pageable);
        Page<Post> postsPage = postEntityPage.map(entityMapper::toPost);
        return PagedResult.from(postsPage);
    }

    @Override
    public Optional<Post> findBySlug(String slug) {
        return jpaPostRepo.findBySlug(slug).map(entityMapper::toPost);
    }
}
