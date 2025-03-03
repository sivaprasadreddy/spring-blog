package com.sivalabs.springblog.web.dtos;

import com.sivalabs.springblog.domain.models.Role;

public record UserDTO(Long id, String name, Role role) {}
