package com.sivalabs.springblog.comment.domain;

import com.sivalabs.springblog.post.domain.PostEntity;
import com.sivalabs.springblog.user.domain.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    // Finds all comments for a specific post.
    List<CommentEntity> findByPost(PostEntity post);

    // Finds all comments created by a specific user.
    List<CommentEntity> findByCreatedBy(UserEntity createdBy);

    // Finds all comments for a specific post and created by a specific user.
    List<CommentEntity> findByPostAndCreatedBy(PostEntity post, UserEntity createdBy);
}
