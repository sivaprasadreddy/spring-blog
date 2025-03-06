package com.sivalabs.springblog.adapter.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailIgnoreCase(String email);
}
