package com.sivalabs.springblog.domain.services;

import com.sivalabs.springblog.domain.data.CommentRepository;
import com.sivalabs.springblog.domain.data.PostRepository;
import com.sivalabs.springblog.domain.data.TagRepository;
import com.sivalabs.springblog.domain.exceptions.ResourceNotFoundException;
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
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;

    public PostService(
            PostRepository postRepository, CommentRepository commentRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional(readOnly = true)
    public PagedResult<Post> getPosts(int pageNo, int pageSize) {
        PagedResult<Post> pagedResult = postRepository.findAllPosts(pageNo, pageSize);
        return loadPostTags(pagedResult);
    }

    @Transactional(readOnly = true)
    public PagedResult<Post> getPostsByCategorySlug(String categorySlug, int pageNo, int pageSize) {
        PagedResult<Post> pagedResult = postRepository.findPostsByCategorySlug(categorySlug, pageNo, pageSize);
        return loadPostTags(pagedResult);
    }

    @Transactional(readOnly = true)
    public PagedResult<Post> getPostsByTagSlug(String tagSlug, int pageNo, int pageSize) {
        PagedResult<Post> pagedResult = postRepository.findPostsByTagSlug(tagSlug, pageNo, pageSize);
        return loadPostTags(pagedResult);
    }

    @Transactional(readOnly = true)
    public Long getPostsCount() {
        return postRepository.findPostsCount();
    }

    private PagedResult<Post> loadPostTags(PagedResult<Post> pagedResult) {
        List<Long> postIds = pagedResult.data().stream().map(Post::getId).toList();
        Map<Long, Set<Tag>> tagsByPostIds = this.getTagsByPostIds(postIds);
        return pagedResult.map(post -> {
            post.setTags(tagsByPostIds.getOrDefault(post.getId(), Set.of()));
            return post;
        });
    }

    @Transactional(readOnly = true)
    public Post getPostBySlug(String slug) {
        var post = postRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with slug: " + slug));
        Set<Tag> tags = this.getTagsByPostIds(List.of(post.getId())).getOrDefault(post.getId(), Set.of());
        post.setTags(tags);
        return post;
    }

    @Transactional(readOnly = true)
    public Post getPostById(Long id) {
        var post = postRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        Set<Tag> tags = this.getTagsByPostIds(List.of(post.getId())).getOrDefault(post.getId(), Set.of());
        post.setTags(tags);
        return post;
    }

    private Map<Long, Set<Tag>> getTagsByPostIds(List<Long> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return Map.of();
        }
        return tagRepository.findTagsByPostIds(postIds);
    }

    @Transactional
    public void createPost(Post post) {
        postRepository.create(post);
    }

    @Transactional
    public void updatePost(Post post) {
        postRepository.update(post);
    }

    @Transactional
    public void deletePostsByIds(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            commentRepository.deleteCommentsByPostIds(ids);
            postRepository.deletePostsByIds(ids);
        }
    }

    @Transactional(readOnly = true)
    public List<Comment> findAllComments() {
        return commentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Transactional
    public void deleteCommentsByIds(List<Long> commentIds) {
        commentRepository.deleteCommentsByIds(commentIds);
    }

    @Transactional
    public Comment createComment(Comment comment) {
        return commentRepository.create(comment);
    }
}
