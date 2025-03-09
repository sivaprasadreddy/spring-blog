package com.sivalabs.springblog.web.forms;

import com.sivalabs.springblog.domain.models.Category;
import com.sivalabs.springblog.domain.models.Post;
import com.sivalabs.springblog.domain.models.PostStatus;
import com.sivalabs.springblog.domain.services.MarkdownUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class EditPostForm {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    private String title;

    @NotBlank(message = "Slug is required")
    private String slug;

    @NotBlank(message = "Short description is required")
    private String shortDescription;

    @NotBlank(message = "Content in Markdown format is required")
    private String contentMarkdown;

    @NotNull(message = "Category is required")
    private Long categoryId;

    @NotNull(message = "Status is required")
    private PostStatus status;

    public EditPostForm() {}

    public EditPostForm(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.slug = post.getSlug();
        this.shortDescription = post.getShortDescription();
        this.contentMarkdown = post.getContentMarkdown();
        this.categoryId = post.getCategory().getId();
        this.status = post.getStatus();
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public Post toPost() {
        Post post = new Post();
        post.setId(this.id);
        post.setTitle(this.title);
        post.setSlug(this.slug);
        post.setShortDescription(this.shortDescription);
        post.setContentMarkdown(this.contentMarkdown);
        post.setContentHtml(MarkdownUtils.toHTML(this.contentMarkdown));
        post.setCategory(new Category(this.categoryId));
        post.setStatus(this.status);
        return post;
    }
}