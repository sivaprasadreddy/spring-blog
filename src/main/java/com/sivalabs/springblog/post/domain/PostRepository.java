package com.sivalabs.springblog.post.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    // Finds a Post by its slug.
    Optional<PostEntity> findBySlug(String slug);

    // Finds all Posts with the given status.
    List<PostEntity> findByStatus(PostEntity.Status status);

    // Finds all Posts created by a specific user.
    List<PostEntity> findByCreatedById(Long userId);
}
