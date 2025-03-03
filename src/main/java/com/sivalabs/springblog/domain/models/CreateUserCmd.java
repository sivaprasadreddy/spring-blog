package com.sivalabs.springblog.domain.models;

public record CreateUserCmd(String name, String email, String password, Role role) {}
