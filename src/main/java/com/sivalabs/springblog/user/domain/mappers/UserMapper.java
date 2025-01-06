package com.sivalabs.springblog.user.domain.mappers;

import com.sivalabs.springblog.user.domain.dtos.UserDTO;
import com.sivalabs.springblog.user.domain.models.UserEntity;

public class UserMapper {
    public static UserDTO toUserDTO(UserEntity userEntity) {
        return new UserDTO(userEntity.getId(), userEntity.getName(), userEntity.getRole());
    }
}
