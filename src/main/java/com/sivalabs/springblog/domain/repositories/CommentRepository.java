package com.sivalabs.springblog.domain.repositories;

import com.sivalabs.springblog.domain.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {}
