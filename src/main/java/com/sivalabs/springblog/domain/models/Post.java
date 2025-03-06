package com.sivalabs.springblog.domain.models;

import java.time.LocalDateTime;

public class Post {
    private Long id;
    private String title;
    private String slug;
    private String shortDescription;
    private String contentMarkdown;
    private String contentHtml;
    private PostStatus status;
    private User createdBy;
    private LocalDateTime createdDate;

    public Post() {}

    public Post(
            Long id,
            String title,
            String slug,
            String shortDescription,
            String contentMarkdown,
            String contentHtml,
            PostStatus status,
            User createdBy,
            LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.shortDescription = shortDescription;
        this.contentMarkdown = contentMarkdown;
        this.contentHtml = contentHtml;
        this.status = status;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getContentMarkdown() {
        return contentMarkdown;
    }

    public void setContentMarkdown(String contentMarkdown) {
        this.contentMarkdown = contentMarkdown;
    }

    public String getContentHtml() {
        return contentHtml;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
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
