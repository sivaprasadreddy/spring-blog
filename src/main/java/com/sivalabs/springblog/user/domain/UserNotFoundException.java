package com.sivalabs.springblog.user.domain;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException forEmail(String email) {
        return new UserNotFoundException("User with email " + email + " not found");
    }
}
