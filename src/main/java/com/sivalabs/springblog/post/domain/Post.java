package com.sivalabs.springblog.post.domain;

import com.sivalabs.springblog.post.domain.models.PostStatus;
import com.sivalabs.springblog.user.domain.dtos.UserDTO;
import java.time.LocalDateTime;

public record Post(
        String title,
        String slug,
        String shortDescription,
        String contentMarkdown,
        String contentHtml,
        PostStatus status,
        UserDTO createdBy,
        LocalDateTime createdDate) {}
