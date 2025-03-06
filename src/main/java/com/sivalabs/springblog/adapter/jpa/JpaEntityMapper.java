package com.sivalabs.springblog.adapter.jpa;

import com.sivalabs.springblog.domain.models.Post;
import com.sivalabs.springblog.domain.models.User;
import org.springframework.stereotype.Component;

@Component
class JpaEntityMapper {

    public Post toPost(PostEntity postEntity) {
        var post = new Post();
        post.setId(postEntity.getId());
        post.setTitle(postEntity.getTitle());
        post.setSlug(postEntity.getSlug());
        post.setShortDescription(postEntity.getShortDescription());
        post.setContentHtml(postEntity.getContentHtml());
        post.setContentMarkdown(postEntity.getContentMarkdown());
        post.setStatus(postEntity.getStatus());
        post.setCreatedDate(postEntity.getCreatedDate());
        if (postEntity.getCreatedBy() != null) {
            post.setCreatedBy(toUser(postEntity.getCreatedBy()));
        }
        return post;
    }

    public User toUser(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getRole());
    }

    public UserEntity toUserEntity(User user) {
        return new UserEntity(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getRole());
    }
}
