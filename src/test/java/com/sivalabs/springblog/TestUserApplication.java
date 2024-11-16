package com.sivalabs.springblog;

import org.springframework.boot.SpringApplication;

public class TestUserApplication {
    public static void main(String[] args) {
        SpringApplication.from(SpringBlogApplication::main)
                .with(ContainersConfig.class)
                .run(args);
    }
}
