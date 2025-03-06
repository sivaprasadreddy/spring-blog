package com.sivalabs.springblog.domain.models;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private String name;
    private String content;
    private Long postId;
    private Long createdUserId;
    private LocalDateTime createdDate;

    public Comment() {}

    public Comment(Long id, String name, String content, Long postId, Long createdUserId, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.postId = postId;
        this.createdUserId = createdUserId;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Long createdUserId) {
        this.createdUserId = createdUserId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
