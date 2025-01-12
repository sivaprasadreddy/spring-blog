package com.sivalabs.springblog.user.domain.repositories;

import java.util.Optional;

import com.sivalabs.springblog.user.domain.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
