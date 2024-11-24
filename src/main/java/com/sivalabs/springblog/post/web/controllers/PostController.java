package com.sivalabs.springblog.post.web.controllers;

import com.sivalabs.springblog.post.domain.PagedResult;
import com.sivalabs.springblog.post.domain.Post;
import com.sivalabs.springblog.post.domain.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping
    PagedResult<Post> getPosts(@RequestParam(name = "page", defaultValue = "1") int pageNo) {
        log.info("Fetching posts for page: {}", pageNo);
        return postService.getPosts(pageNo);
    }

}
