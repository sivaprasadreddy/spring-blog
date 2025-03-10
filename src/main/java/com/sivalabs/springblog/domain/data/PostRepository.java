package com.sivalabs.springblog.domain.data;

import com.sivalabs.springblog.domain.models.PagedResult;
import com.sivalabs.springblog.domain.models.Post;
import com.sivalabs.springblog.domain.models.Tag;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository {

    PagedResult<Post> findAllPosts(int pageNo, int pageSize);

    PagedResult<Post> findPostsByCategorySlug(String categorySlug, int pageNo, int pageSize);

    PagedResult<Post> findPostsByTagSlug(String tagSlug, int pageNo, int pageSize);

    Optional<Post> findBySlug(String slug);

    Optional<Post> findById(Long id);

    void deletePostsByIds(List<Long> ids);

    Long create(Post post);

    void update(Post post);

    void addPostTags(Long postId, Set<Tag> tags);

    Long findPostsCount();
}
