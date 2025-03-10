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
        model.addAttribute("categories", postService.findAllCategories());
        model.addAttribute("tags", postService.findAllTags());
        return "blog/posts";
    }

    @GetMapping("/category/{slug}")
    String getPostsByCategory(
            @PathVariable String slug, @RequestParam(name = "page", defaultValue = "1") int pageNo, Model model) {
        log.info("Fetching posts for category slug: {} and page: {}", slug, pageNo);
        PagedResult<Post> pagedResult = postService.getPostsByCategorySlug(slug, pageNo, properties.pageSize());
        model.addAttribute("pagedResult", pagedResult);
        model.addAttribute("categories", postService.findAllCategories());
        model.addAttribute("tags", postService.findAllTags());
        model.addAttribute("categorySlug", slug);
        return "blog/posts";
    }

    @GetMapping("/tag/{slug}")
    String getPostsByTag(
            @PathVariable String slug, @RequestParam(name = "page", defaultValue = "1") int pageNo, Model model) {
        log.info("Fetching posts for tag slug: {} and page: {}", slug, pageNo);
        PagedResult<Post> pagedResult = postService.getPostsByTagSlug(slug, pageNo, properties.pageSize());
        model.addAttribute("pagedResult", pagedResult);
        model.addAttribute("categories", postService.findAllCategories());
        model.addAttribute("tags", postService.findAllTags());
        model.addAttribute("tagSlug", slug);
        return "blog/posts";
    }

    @GetMapping("/{slug}")
    String getPostDetails(@PathVariable String slug, Model model) {
        log.info("Fetching post details for slug: {}", slug);
        Post post = postService.getPostBySlug(slug);
        model.addAttribute("post", post);
        model.addAttribute("categories", postService.findAllCategories());
        model.addAttribute("tags", postService.findAllTags());
        model.addAttribute("tagSlug", null);
        return "blog/post-details";
    }
}
