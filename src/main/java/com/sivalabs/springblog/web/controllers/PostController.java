package com.sivalabs.springblog.web.controllers;

import com.sivalabs.springblog.ApplicationProperties;
import com.sivalabs.springblog.domain.models.PagedResult;
import com.sivalabs.springblog.domain.models.Post;
import com.sivalabs.springblog.domain.services.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/posts")
class PostController {
    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;
    private final ApplicationProperties properties;

    PostController(PostService postService, ApplicationProperties properties) {
        this.postService = postService;
        this.properties = properties;
    }

    @GetMapping
    String getPosts(@RequestParam(name = "page", defaultValue = "1") int pageNo, Model model) {
        log.info("Fetching posts for page: {}", pageNo);
        PagedResult<Post> pagedResult = postService.getPosts(pageNo, properties.pageSize());
        model.addAttribute("pagedResult", pagedResult);
        return "blog/posts";
    }

    @GetMapping("/{slug}")
    String getPostDetails(@PathVariable String slug, Model model) {
        log.info("Fetching post details for slug: {}", slug);
        Post post = postService.getPostBySlug(slug);
        model.addAttribute("post", post);
        return "blog/post-details";
    }
}
