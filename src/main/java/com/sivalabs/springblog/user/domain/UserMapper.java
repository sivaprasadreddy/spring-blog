package com.sivalabs.springblog.user.domain;

public class UserMapper {
    public static UserDTO toUserDTO(UserEntity userEntity) {
        return new UserDTO(userEntity.getId(), userEntity.getName(), userEntity.getRole());
    }
}
