package com.sivalabs.springblog.post.domain;

import com.sivalabs.springblog.user.domain.UserDTO;
import com.sivalabs.springblog.user.domain.UserMapper;

class PostMapper {

    static Post toPost(PostEntity postEntity) {

        UserDTO createdBy = postEntity.getCreatedBy() != null ? UserMapper.toUserDTO(postEntity.getCreatedBy()) : null;

        return new Post(
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
