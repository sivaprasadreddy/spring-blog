package com.sivalabs.springblog.web.mappers;

import com.sivalabs.springblog.domain.entities.UserEntity;
import com.sivalabs.springblog.web.dtos.UserDTO;

public class UserMapper {
    public static UserDTO toUserDTO(UserEntity userEntity) {
        return new UserDTO(userEntity.getId(), userEntity.getName(), userEntity.getRole());
    }
}
