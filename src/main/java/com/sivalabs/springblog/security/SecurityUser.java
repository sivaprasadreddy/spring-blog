package com.sivalabs.springblog.security;

import com.sivalabs.springblog.user.domain.models.UserEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.Set;

public class SecurityUser extends org.springframework.security.core.userdetails.User {

    private final String name;
    private final Long id;


    public SecurityUser(UserEntity user) {
        super(
                user.getEmail(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                Set.of(new SimpleGrantedAuthority(user.getRole().name())));

        this.name = user.getName();
        this.id = user.getId();
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }




}
