package com.sivalabs.springblog.domain.data;

import com.sivalabs.springblog.domain.models.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    List<Comment> findAll();

    Optional<Comment> findById(Long id);

    List<Comment> findByPostId(Long postId);

    Comment create(Comment comment);

    void deleteById(Long id);

    void deleteCommentsByIds(List<Long> ids);

    void deleteCommentsByPostIds(List<Long> ids);
}
