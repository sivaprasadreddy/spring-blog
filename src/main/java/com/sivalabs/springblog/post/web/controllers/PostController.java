package com.sivalabs.springblog.post.web.controllers;

import com.sivalabs.springblog.common.PagedResult;
import com.sivalabs.springblog.post.domain.Post;
import com.sivalabs.springblog.post.domain.PostService;
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
public class PostController {
    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String getPosts(@RequestParam(name = "page", defaultValue = "1") int pageNo, Model model) {
        log.info("Fetching posts for page: {}", pageNo);
        PagedResult<Post> pagedResult = postService.getPosts(pageNo);
        model.addAttribute("pagedResult", pagedResult);
        return "posts";
    }

    @GetMapping("/{slug}")
    public String getPostDetails(@PathVariable String slug, Model model) {
        log.info("Fetching post details for slug: {}", slug);
        Post post = postService.getPostBySlug(slug);
        model.addAttribute("post", post);
        return "post-details";
    }
}
