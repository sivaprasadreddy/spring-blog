package com.sivalabs.springblog.domain.data;

import com.sivalabs.springblog.domain.models.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment create(Comment comment);

    Optional<Comment> findById(Long id);

    List<Comment> findByPostId(Long postId);

    List<Comment> findAll();

    void deleteById(Long id);
}
