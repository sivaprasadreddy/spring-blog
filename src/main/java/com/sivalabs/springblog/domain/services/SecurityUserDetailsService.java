package com.sivalabs.springblog.domain.services;

import com.sivalabs.springblog.domain.models.SecurityUser;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public SecurityUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<SecurityUser> securityUser = userService.findByEmail(username).map(SecurityUser::new);
        if (securityUser.isEmpty()) {
            throw new UsernameNotFoundException("No user found with username " + username);
        }
        return securityUser.orElseThrow();
    }
}
