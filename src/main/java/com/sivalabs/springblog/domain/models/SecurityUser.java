package com.sivalabs.springblog.domain.models;

import java.util.Set;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class SecurityUser extends org.springframework.security.core.userdetails.User {

    private final String name;
    private final Long id;

    public SecurityUser(User user) {
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
