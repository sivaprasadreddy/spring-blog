package com.sivalabs.springblog.security;


import com.sivalabs.springblog.user.domain.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("userDetailsService")
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public SecurityUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<SecurityUser> securityUser =
                userRepository.findByEmail(username).map(SecurityUser::new);
        if (securityUser.isEmpty()) {
            throw new UsernameNotFoundException("No user found with username " + username);
        }
        return securityUser.orElseThrow();
    }


}
