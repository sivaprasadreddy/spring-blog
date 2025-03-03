package com.sivalabs.springblog.web.mappers;

import com.sivalabs.springblog.domain.entities.PostEntity;
import com.sivalabs.springblog.web.dtos.PostDTO;
import com.sivalabs.springblog.web.dtos.UserDTO;

public class PostMapper {

    public static PostDTO toPost(PostEntity postEntity) {

        UserDTO createdBy = postEntity.getCreatedBy() != null ? UserMapper.toUserDTO(postEntity.getCreatedBy()) : null;

        return new PostDTO(
                postEntity.getTitle(),
                postEntity.getSlug(),
                postEntity.getShortDescription(),
                postEntity.getContentMarkdown(),
                postEntity.getContentHtml(),
                postEntity.getStatus(),
                createdBy,
                postEntity.getCreatedDate());
    }
}
