package com.sivalabs.springblog.adapter.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

interface JpaCommentRepository extends JpaRepository<CommentEntity, Long> {}
