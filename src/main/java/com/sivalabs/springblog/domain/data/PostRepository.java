package com.sivalabs.springblog.domain.data;

import com.sivalabs.springblog.domain.models.PagedResult;
import com.sivalabs.springblog.domain.models.Post;
import java.util.List;
import java.util.Optional;

public interface PostRepository {

    PagedResult<Post> findAllPosts(int pageNo, int pageSize);

    Optional<Post> findBySlug(String slug);

    void deletePostsByIds(List<Long> ids);
}
