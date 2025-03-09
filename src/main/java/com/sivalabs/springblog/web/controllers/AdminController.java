package com.sivalabs.springblog.web.controllers;

import com.sivalabs.springblog.ApplicationProperties;
import com.sivalabs.springblog.domain.models.*;
import com.sivalabs.springblog.domain.services.PostService;
import com.sivalabs.springblog.web.forms.CreatePostForm;
import com.sivalabs.springblog.web.forms.EditPostForm;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    private final PostService postService;
    private final ApplicationProperties properties;

    public AdminController(PostService postService, ApplicationProperties properties) {
        this.postService = postService;
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
        List<Comment> comments = postService.findAllComments();
        model.addAttribute("comments", comments);
        return "admin/comments";
    }

    @GetMapping("/categories")
    public String getAllCategories(Model model) {
        log.info("Fetching all categories for admin view");
        List<Category> categories = postService.findAllCategories();
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

    @PostMapping("/comments/delete")
    public String deleteSelectedComments(
            @RequestParam(value = "commentIds", required = false) List<Long> commentIds,
            RedirectAttributes redirectAttributes) {
        log.info("Deleting comments with IDs: {}", commentIds);
        if (commentIds != null && !commentIds.isEmpty()) {
            postService.deleteCommentsByIds(commentIds);
            redirectAttributes.addFlashAttribute("message", "Selected comments have been deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "No comments selected for deletion");
        }
        return "redirect:/admin/comments";
    }

    @GetMapping("/posts/create")
    public String showCreatePostForm(Model model) {
        log.info("Showing create post form");
        model.addAttribute("post", new CreatePostForm());
        model.addAttribute("categories", postService.findAllCategories());
        model.addAttribute("statuses", PostStatus.values());
        return "admin/create-post";
    }

    @PostMapping("/posts/create")
    public String createPost(
            @ModelAttribute("post") @Valid CreatePostForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        log.info("Creating new post: {}", form.getTitle());

        if (bindingResult.hasErrors()) {
            log.info("Validation errors in post creation form");
            model.addAttribute("categories", postService.findAllCategories());
            model.addAttribute("statuses", PostStatus.values());
            return "admin/create-post";
        }

        User user = new User(UserContextUtils.getCurrentUserIdOrThrow());
        Post post = form.toPost(user);

        postService.createPost(post);
        redirectAttributes.addFlashAttribute("message", "Post created successfully");
        return "redirect:/admin/posts";
    }

    @GetMapping("/posts/edit/{id}")
    public String showEditPostForm(@PathVariable Long id, Model model) {
        log.info("Showing edit post form for post with ID: {}", id);
        Post post = postService.getPostById(id);
        EditPostForm form = new EditPostForm(post);

        model.addAttribute("post", form);
        model.addAttribute("categories", postService.findAllCategories());
        model.addAttribute("statuses", PostStatus.values());
        return "admin/edit-post";
    }

    @PostMapping("/posts/edit")
    public String updatePost(
            @ModelAttribute("post") @Valid EditPostForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        log.info("Updating post with ID: {}", form.getId());

        if (bindingResult.hasErrors()) {
            log.info("Validation errors in post edit form");
            model.addAttribute("categories", postService.findAllCategories());
            model.addAttribute("statuses", PostStatus.values());
            return "admin/edit-post";
        }

        Post post = form.toPost();
        postService.updatePost(post);
        redirectAttributes.addFlashAttribute("message", "Post updated successfully");
        return "redirect:/admin/posts";
    }
}
