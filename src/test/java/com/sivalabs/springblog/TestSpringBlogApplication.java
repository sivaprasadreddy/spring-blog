package com.sivalabs.springblog;

import org.springframework.boot.SpringApplication;

public class TestSpringBlogApplication {
    public static void main(String[] args) {
        System.setProperty("spring.docker.compose.enabled", "false");
        SpringApplication.from(SpringBlogApplication::main)
                .with(TestcontainersConfig.class)
                .run(args);
    }
}
