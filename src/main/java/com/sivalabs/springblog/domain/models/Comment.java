package com.sivalabs.springblog.domain.models;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private String content;
    private Long postId;
    private User createdBy;
    private LocalDateTime createdDate;

    public Comment() {}

    public Comment(Long id) {
        this.id = id;
    }

    public Comment(Long id, String content, Long postId, User createdBy, LocalDateTime createdDate) {
        this.id = id;
        this.content = content;
        this.postId = postId;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
