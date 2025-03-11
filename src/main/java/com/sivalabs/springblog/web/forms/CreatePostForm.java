package com.sivalabs.springblog.web.forms;

import com.sivalabs.springblog.domain.models.Category;
import com.sivalabs.springblog.domain.models.Post;
import com.sivalabs.springblog.domain.models.PostStatus;
import com.sivalabs.springblog.domain.models.User;
import com.sivalabs.springblog.domain.services.MarkdownUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

public class CreatePostForm {
    @NotBlank(message = "Title is required") private String title;

    @NotBlank(message = "Slug is required") private String slug;

    @NotBlank(message = "Short description is required") private String shortDescription;

    @NotBlank(message = "Content in Markdown format is required") private String contentMarkdown;

    @NotNull(message = "Category is required") private Long categoryId;

    private String tags;

    @NotNull(message = "Status is required") private PostStatus status;

    public CreatePostForm() {}

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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public Post toPost(User user) {
        Post post = new Post();
        post.setTitle(this.title);
        post.setSlug(this.slug);
        post.setShortDescription(this.shortDescription);
        post.setContentMarkdown(this.contentMarkdown);
        post.setContentHtml(MarkdownUtils.toHTML(this.contentMarkdown));
        post.setCategory(new Category(this.categoryId));
        post.setStatus(this.status);
        post.setCreatedBy(user);
        post.setCreatedDate(LocalDateTime.now());
        post.setTags(Set.of());
        return post;
    }
}
