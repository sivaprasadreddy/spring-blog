package com.sivalabs.springblog.web.controllers;

import com.sivalabs.springblog.ApplicationProperties;
import com.sivalabs.springblog.domain.data.CategoryRepository;
import com.sivalabs.springblog.domain.data.CommentRepository;
import com.sivalabs.springblog.domain.models.Category;
import com.sivalabs.springblog.domain.models.Comment;
import com.sivalabs.springblog.domain.models.PagedResult;
import com.sivalabs.springblog.domain.models.Post;
import com.sivalabs.springblog.domain.services.PostService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    private final PostService postService;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;
    private final ApplicationProperties properties;

    public AdminController(
            PostService postService,
            CommentRepository commentRepository,
            CategoryRepository categoryRepository,
            ApplicationProperties properties) {
        this.postService = postService;
        this.commentRepository = commentRepository;
        this.categoryRepository = categoryRepository;
        this.properties = properties;
    }

    @GetMapping
    public String adminDashboard() {
        return "admin/index";
    }

    @GetMapping("/posts")
    public String getAllPosts(@RequestParam(name = "page", defaultValue = "1") int pageNo, Model model) {
        log.info("Fetching all posts for admin view, page: {}", pageNo);
        PagedResult<Post> pagedResult = postService.getPosts(pageNo, properties.pageSize());
        model.addAttribute("pagedResult", pagedResult);
        return "admin/posts";
    }

    @GetMapping("/comments")
    public String getAllComments(Model model) {
        log.info("Fetching all comments for admin view");
        List<Comment> comments = commentRepository.findAll();
        model.addAttribute("comments", comments);
        return "admin/comments";
    }

    @GetMapping("/categories")
    public String getAllCategories(Model model) {
        log.info("Fetching all categories for admin view");
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "admin/categories";
    }

    @PostMapping("/posts/delete")
    public String deleteSelectedPosts(
            @RequestParam(value = "postIds", required = false) List<Long> postIds,
            RedirectAttributes redirectAttributes) {
        log.info("Deleting posts with IDs: {}", postIds);
        if (postIds != null && !postIds.isEmpty()) {
            postService.deletePostsByIds(postIds);
            redirectAttributes.addFlashAttribute("message", "Selected posts have been deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "No posts selected for deletion");
        }
        return "redirect:/admin/posts";
    }
}
