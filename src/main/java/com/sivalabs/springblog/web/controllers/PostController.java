package com.sivalabs.springblog.web.controllers;

import com.sivalabs.springblog.domain.entities.PostEntity;
import com.sivalabs.springblog.domain.models.PagedResult;
import com.sivalabs.springblog.domain.services.PostService;
import com.sivalabs.springblog.web.dtos.PostDTO;
import com.sivalabs.springblog.web.mappers.PostMapper;
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
        PagedResult<PostEntity> pagedResult = postService.getPosts(pageNo);
        PagedResult<PostDTO> pagedPostDTOs = pagedResult.map(PostMapper::toPost);
        model.addAttribute("pagedResult", pagedPostDTOs);
        return "blog/posts";
    }

    @GetMapping("/{slug}")
    public String getPostDetails(@PathVariable String slug, Model model) {
        log.info("Fetching post details for slug: {}", slug);
        PostEntity postEntity = postService.getPostBySlug(slug);
        PostDTO postDTO = PostMapper.toPost(postEntity);
        model.addAttribute("post", postDTO);
        return "blog/post-details";
    }
}
