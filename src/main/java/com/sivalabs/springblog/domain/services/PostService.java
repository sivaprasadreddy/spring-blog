package com.sivalabs.springblog.domain.services;

import com.sivalabs.springblog.domain.data.CategoryRepository;
import com.sivalabs.springblog.domain.data.CommentRepository;
import com.sivalabs.springblog.domain.data.PostRepository;
import com.sivalabs.springblog.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.springblog.domain.models.Category;
import com.sivalabs.springblog.domain.models.Comment;
import com.sivalabs.springblog.domain.models.PagedResult;
import com.sivalabs.springblog.domain.models.Post;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;

    public PostService(
            PostRepository postRepository, CommentRepository commentRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.categoryRepository = categoryRepository;
    }

    public PagedResult<Post> getPosts(int pageNo, int pageSize) {
        return postRepository.findAllPosts(pageNo, pageSize);
    }

    public PagedResult<Post> getPostsByCategorySlug(String categorySlug, int pageNo, int pageSize) {
        return postRepository.findPostsByCategorySlug(categorySlug, pageNo, pageSize);
    }

    public Post getPostBySlug(String slug) {
        return postRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with slug: " + slug));
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

    public Post getPostById(Long id) {
        return postRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
    }

    @Transactional
    public void updatePost(Post post) {
        postRepository.update(post);
    }
}
