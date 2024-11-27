package com.sivalabs.springblog.post.domain;

import com.sivalabs.springblog.ApplicationProperties;
import com.sivalabs.springblog.common.PagedResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final ApplicationProperties properties;

    public PostService(PostRepository postRepository, ApplicationProperties properties) {
        this.postRepository = postRepository;
        this.properties = properties;
    }

    public PagedResult<Post> getPosts(int pageNo) {
        Sort sort = Sort.by("created_date").ascending();
        pageNo = pageNo <= 1 ? 0 : pageNo - 1;
        Pageable pageable = PageRequest.of(pageNo, properties.pageSize(), sort);
        Page<Post> productsPage = postRepository.findAll(pageable).map(PostMapper::toPost);

        return new PagedResult<>(
                productsPage.getContent(),
                productsPage.getTotalElements(),
                productsPage.getNumber() + 1,
                productsPage.getTotalPages(),
                productsPage.isFirst(),
                productsPage.isLast(),
                productsPage.hasNext(),
                productsPage.hasPrevious());
    }
}
