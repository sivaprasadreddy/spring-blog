package com.sivalabs.springblog.domain.services;

import com.sivalabs.springblog.domain.data.CategoryRepository;
import com.sivalabs.springblog.domain.data.CommentRepository;
import com.sivalabs.springblog.domain.data.PostRepository;
import com.sivalabs.springblog.domain.data.TagRepository;
import com.sivalabs.springblog.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.springblog.domain.models.Category;
import com.sivalabs.springblog.domain.models.Comment;
import com.sivalabs.springblog.domain.models.PagedResult;
import com.sivalabs.springblog.domain.models.Post;
import com.sivalabs.springblog.domain.models.Tag;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public PostService(
            PostRepository postRepository,
            CommentRepository commentRepository,
            CategoryRepository categoryRepository,
            TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public PagedResult<Post> getPosts(int pageNo, int pageSize) {
        PagedResult<Post> pagedResult = postRepository.findAllPosts(pageNo, pageSize);
        return loadPostTags(pagedResult);
    }

    public PagedResult<Post> getPostsByCategorySlug(String categorySlug, int pageNo, int pageSize) {
        PagedResult<Post> pagedResult = postRepository.findPostsByCategorySlug(categorySlug, pageNo, pageSize);
        return loadPostTags(pagedResult);
    }

    private PagedResult<Post> loadPostTags(PagedResult<Post> pagedResult) {
        List<Long> postIds = pagedResult.data().stream().map(Post::getId).toList();
        Map<Long, Set<Tag>> tagsByPostIds = this.getTagsByPostIds(postIds);
        return pagedResult.map(post -> {
            post.setTags(tagsByPostIds.getOrDefault(post.getId(), Set.of()));
            return post;
        });
    }

    public Post getPostBySlug(String slug) {
        var post = postRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with slug: " + slug));
        Set<Tag> tags = this.getTagsByPostIds(List.of(post.getId())).getOrDefault(post.getId(), Set.of());
        post.setTags(tags);
        return post;
    }

    public Post getPostById(Long id) {
        var post = postRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        Set<Tag> tags = this.getTagsByPostIds(List.of(post.getId())).getOrDefault(post.getId(), Set.of());
        post.setTags(tags);
        return post;
    }

    @Transactional
    public void deletePostsByIds(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            postRepository.deletePostsByIds(ids);
        }
    }

    @Transactional
    public void deleteCommentsByIds(List<Long> commentIds) {
        commentRepository.deleteCommentsByIds(commentIds);
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Comment> findAllComments() {
        return commentRepository.findAll();
    }

    @Transactional
    public void createPost(Post post) {
        postRepository.create(post);
    }

    @Transactional
    public void updatePost(Post post) {
        postRepository.update(post);
    }

    /**
     * Fetches associated tags for the given list of postIds.
     *
     * @param postIds List of post IDs
     * @return Map where key is postId and value is a Set of Tags associated with that post
     */
    public Map<Long, Set<Tag>> getTagsByPostIds(List<Long> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return Map.of();
        }
        return tagRepository.findTagsByPostIds(postIds);
    }
}
