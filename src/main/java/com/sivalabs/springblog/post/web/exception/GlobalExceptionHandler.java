package com.sivalabs.springblog.post.web.exception;

import com.sivalabs.springblog.post.domain.PostNotFoundException;
import java.net.URI;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final URI NOT_FOUND_TYPE = URI.create("https://api.springblog.com/errors/not-found");
    private static final URI ISE_FOUND_TYPE = URI.create("https://api.springblog.com/errors/server-error");
    private static final String SERVICE_NAME = "post-service";

    @ExceptionHandler(Exception.class)
    public ModelAndView handleUnhandledException(Exception e, Model model) {
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("error", "Internal Server Error");
        model.addAttribute("message", e.getMessage());
        model.addAttribute("timestamp", Instant.now());
        model.addAttribute("type", ISE_FOUND_TYPE);
        model.addAttribute("service", SERVICE_NAME);
        return new ModelAndView("error", model.asMap());
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ModelAndView handlePostNotFoundException(PostNotFoundException e, Model model) {
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", "Post Not Found");
        model.addAttribute("message", e.getMessage());
        model.addAttribute("timestamp", Instant.now());
        model.addAttribute("type", NOT_FOUND_TYPE);
        model.addAttribute("service", SERVICE_NAME);
        return new ModelAndView("error", model.asMap());
    }
}
