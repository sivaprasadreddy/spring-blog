package com.sivalabs.springblog.web.mappers;

import com.sivalabs.springblog.domain.models.User;
import com.sivalabs.springblog.web.dtos.UserDTO;

public class UserMapper {
    public static UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getRole());
    }
}
