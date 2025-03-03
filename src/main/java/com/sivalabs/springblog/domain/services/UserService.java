package com.sivalabs.springblog.domain.services;

import com.sivalabs.springblog.domain.entities.UserEntity;
import com.sivalabs.springblog.domain.models.CreateUserCmd;
import com.sivalabs.springblog.domain.repositories.UserRepository;
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

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Transactional
    public void createUser(CreateUserCmd cmd) {
        var user = new UserEntity();
        user.setName(cmd.name());
        user.setEmail(cmd.email());
        user.setPassword(passwordEncoder.encode(cmd.password()));
        user.setRole(cmd.role());
        userRepository.save(user);
    }
}
