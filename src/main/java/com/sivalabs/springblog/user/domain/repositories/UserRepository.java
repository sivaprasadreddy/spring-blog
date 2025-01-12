package com.sivalabs.springblog.user.domain.repositories;

import com.sivalabs.springblog.user.domain.models.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
