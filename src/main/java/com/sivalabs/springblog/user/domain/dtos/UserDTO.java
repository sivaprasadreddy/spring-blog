package com.sivalabs.springblog.user.domain.dtos;

import com.sivalabs.springblog.user.domain.models.RoleEnum;

public record UserDTO(Long id, String name, RoleEnum role) {}
