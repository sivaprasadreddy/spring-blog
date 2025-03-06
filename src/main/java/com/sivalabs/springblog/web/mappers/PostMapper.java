package com.sivalabs.springblog.web.mappers;

import com.sivalabs.springblog.domain.models.Post;
import com.sivalabs.springblog.web.dtos.PostDTO;
import com.sivalabs.springblog.web.dtos.UserDTO;

public class PostMapper {

    public static PostDTO toPost(Post post) {

        UserDTO createdBy = post.getCreatedBy() != null ? UserMapper.toUserDTO(post.getCreatedBy()) : null;

        return new PostDTO(
                post.getTitle(),
                post.getSlug(),
                post.getShortDescription(),
                post.getContentMarkdown(),
                post.getContentHtml(),
                post.getStatus(),
                createdBy,
                post.getCreatedDate());
    }
}
