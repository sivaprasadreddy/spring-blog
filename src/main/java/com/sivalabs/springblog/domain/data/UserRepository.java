package com.sivalabs.springblog.domain.data;

import com.sivalabs.springblog.domain.models.User;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    void create(User user);
}
