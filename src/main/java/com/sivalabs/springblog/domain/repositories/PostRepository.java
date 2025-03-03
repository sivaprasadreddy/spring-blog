package com.sivalabs.springblog.domain.repositories;

import com.sivalabs.springblog.domain.entities.PostEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    @Query("SELECT p FROM PostEntity p join fetch p.createdBy")
    Page<PostEntity> findAllPosts(Pageable pageable);

    @Query("SELECT p FROM PostEntity p join fetch p.createdBy where p.slug = :slug")
    Optional<PostEntity> findBySlug(@Param("slug") String slug);
}
