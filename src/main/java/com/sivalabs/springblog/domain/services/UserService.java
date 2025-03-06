package com.sivalabs.springblog.domain.services;

import com.sivalabs.springblog.domain.data.UserRepository;
import com.sivalabs.springblog.domain.models.CreateUserCmd;
import com.sivalabs.springblog.domain.models.User;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void createUser(CreateUserCmd cmd) {
        var user = new User();
        user.setName(cmd.name());
        user.setEmail(cmd.email());
        user.setPassword(passwordEncoder.encode(cmd.password()));
        user.setRole(cmd.role());
        userRepository.create(user);
    }
}
