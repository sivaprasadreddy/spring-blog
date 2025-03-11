package com.sivalabs.springblog.web.controllers;

import com.sivalabs.springblog.ApplicationProperties;
import com.sivalabs.springblog.domain.models.Category;
import com.sivalabs.springblog.domain.models.Comment;
import com.sivalabs.springblog.domain.models.PagedResult;
import com.sivalabs.springblog.domain.models.Post;
import com.sivalabs.springblog.domain.models.Tag;
import com.sivalabs.springblog.domain.models.User;
import com.sivalabs.springblog.domain.services.CategoryService;
import com.sivalabs.springblog.domain.services.PostService;
import com.sivalabs.springblog.domain.services.TagService;
import com.sivalabs.springblog.web.forms.CommentForm;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
class PostController {
    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final ApplicationProperties properties;

    PostController(
            PostService postService,
            CategoryService categoryService,
            TagService tagService,
            ApplicationProperties properties) {
        this.postService = postService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.properties = properties;
    }

    @GetMapping
    String getPosts(@RequestParam(name = "page", defaultValue = "1") int pageNo, Model model) {
        log.info("Fetching posts for page: {}", pageNo);
        PagedResult<Post> pagedResult = postService.getPosts(pageNo, properties.pageSize());
        model.addAttribute("pagedResult", pagedResult);
        return "blog/posts";
    }

    @GetMapping("/category/{slug}")
    String getPostsByCategory(
            @PathVariable String slug, @RequestParam(name = "page", defaultValue = "1") int pageNo, Model model) {
        log.info("Fetching posts for category slug: {} and page: {}", slug, pageNo);
        PagedResult<Post> pagedResult = postService.getPostsByCategorySlug(slug, pageNo, properties.pageSize());
        model.addAttribute("pagedResult", pagedResult);
        model.addAttribute("categorySlug", slug);
        return "blog/posts";
    }

    @GetMapping("/tag/{slug}")
    String getPostsByTag(
            @PathVariable String slug, @RequestParam(name = "page", defaultValue = "1") int pageNo, Model model) {
        log.info("Fetching posts for tag slug: {} and page: {}", slug, pageNo);
        PagedResult<Post> pagedResult = postService.getPostsByTagSlug(slug, pageNo, properties.pageSize());
        model.addAttribute("pagedResult", pagedResult);
        model.addAttribute("tagSlug", slug);
        return "blog/posts";
    }

    @GetMapping("/{slug}")
    String getPostDetails(@PathVariable String slug, Model model) {
        log.info("Fetching post details for slug: {}", slug);
        Post post = postService.getPostBySlug(slug);
        List<Comment> comments = postService.findCommentsByPostId(post.getId());
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("tagSlug", null);
        model.addAttribute("commentForm", new CommentForm());
        return "blog/post-details";
    }

    @ModelAttribute("categories")
    List<Category> allCategories() {
        return categoryService.findAllCategories();
    }

    @ModelAttribute("tags")
    List<Tag> allTags() {
        return tagService.findAllTags();
    }

    @PostMapping("/{slug}/comments")
    String addComment(@PathVariable String slug, @Valid CommentForm commentForm, Model model) {
        log.info("Adding comment to post with slug: {}", slug);
        Post post = postService.getPostBySlug(slug);
        commentForm.setPostId(post.getId());
        User user = new User(UserContextUtils.getCurrentUserIdOrThrow());
        Comment comment = commentForm.toComment(user);
        postService.createComment(comment);
        return "redirect:/posts/" + slug;
    }
}
