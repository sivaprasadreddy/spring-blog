package com.sivalabs.springblog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.springblog.domain.models.Category;
import com.sivalabs.springblog.domain.models.Post;
import com.sivalabs.springblog.domain.models.PostStatus;
import com.sivalabs.springblog.domain.models.Tag;
import com.sivalabs.springblog.domain.services.MarkdownUtils;
import com.sivalabs.springblog.domain.services.PostService;
import com.sivalabs.springblog.domain.services.UserService;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    private final PostService postService;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    public DataLoader(PostService postService, ObjectMapper objectMapper, UserService userService) {
        this.postService = postService;
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        Long count = postService.getPostsCount();
        if (count > 0) {
            log.info("Posts already loaded. Skipping loading posts from json file.");
            return;
        }
        InputStream inputStream = new ClassPathResource("posts.json").getInputStream();
        PostEntries postEntries = objectMapper.readValue(inputStream, PostEntries.class);
        // System.out.println("postEntries = " + postEntries.posts);
        for (PostEntry postEntry : postEntries.posts) {
            String markdownFile = postEntry.markdownFile();
            String mdContent = new ClassPathResource("data/" + markdownFile).getContentAsString(StandardCharsets.UTF_8);
            // System.out.println("Title: "+ postEntry.title());
            // System.out.println("mdContent = " + mdContent);
            String html = MarkdownUtils.toHTML(mdContent);
            // System.out.println("html = " + html);

            Set<Tag> tags = postEntry.tags().stream()
                    .map(postService::getOrCreateTagByName)
                    .collect(Collectors.toSet());
            Category category = postService.getOrCreateCategoryByName(postEntry.category());
            Post post = new Post(
                    null,
                    postEntry.title(),
                    postEntry.slug(),
                    postEntry.shortDescription(),
                    mdContent,
                    html,
                    category,
                    tags,
                    PostStatus.PUBLISHED,
                    userService.findByEmail("siva@gmail.com").orElseThrow(),
                    LocalDateTime.now());
            postService.createPost(post);
        }
    }

    record PostEntries(List<PostEntry> posts) {}

    record PostEntry(
            String title,
            String slug,
            String shortDescription,
            String markdownFile,
            String category,
            Set<String> tags) {}
}
