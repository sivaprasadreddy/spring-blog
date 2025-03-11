package com.sivalabs.springblog.web.forms;

import com.sivalabs.springblog.domain.models.Comment;
import com.sivalabs.springblog.domain.models.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CommentForm {
    @NotBlank(message = "Comment content is required") private String content;

    @NotNull(message = "Post ID is required") private Long postId;

    public CommentForm() {}

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

    public Comment toComment(User user) {
        Comment comment = new Comment();
        comment.setContent(this.content);
        comment.setPostId(this.postId);
        comment.setCreatedBy(user);
        comment.setCreatedDate(LocalDateTime.now());
        return comment;
    }
}
