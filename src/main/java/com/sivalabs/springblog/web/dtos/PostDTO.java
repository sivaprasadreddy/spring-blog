package com.sivalabs.springblog.web.dtos;

import com.sivalabs.springblog.domain.models.PostStatus;
import java.time.LocalDateTime;

public record PostDTO(
        String title,
        String slug,
        String shortDescription,
        String contentMarkdown,
        String contentHtml,
        PostStatus status,
        UserDTO createdBy,
        LocalDateTime createdDate) {}
