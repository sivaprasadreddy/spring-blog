package com.sivalabs.springblog.adapter.jpa;

import com.sivalabs.springblog.domain.data.UserRepository;
import com.sivalabs.springblog.domain.models.User;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class UserRepositoryJpaImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    private final JpaEntityMapper jpaEntityMapper;

    UserRepositoryJpaImpl(JpaUserRepository jpaUserRepository, JpaEntityMapper jpaEntityMapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.jpaEntityMapper = jpaEntityMapper;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmailIgnoreCase(email).map(jpaEntityMapper::toUser);
    }

    @Override
    public void create(User user) {
        var userEntity = jpaEntityMapper.toUserEntity(user);
        jpaUserRepository.save(userEntity);
    }
}
