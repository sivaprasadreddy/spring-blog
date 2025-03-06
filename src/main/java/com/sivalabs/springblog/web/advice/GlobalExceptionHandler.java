package com.sivalabs.springblog.web.advice;

import com.sivalabs.springblog.domain.exceptions.ResourceNotFoundException;
import java.time.Instant;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle(Exception e, Model model) {
        model.addAttribute("error", "Internal Server Error");
        model.addAttribute("message", e.getMessage());
        model.addAttribute("timestamp", Instant.now());
        return new ModelAndView("error/500", model.asMap());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ModelAndView handle(ResourceNotFoundException e, Model model) {
        model.addAttribute("error", "Resource Not Found");
        model.addAttribute("message", e.getMessage());
        model.addAttribute("timestamp", Instant.now());
        return new ModelAndView("error/404", model.asMap());
    }

    @ExceptionHandler(AccessDeniedException.class)
    ModelAndView handle(AccessDeniedException e, Model model) {
        model.addAttribute("error", "Access Denied");
        model.addAttribute("message", e.getMessage());
        model.addAttribute("timestamp", Instant.now());
        return new ModelAndView("error/403", model.asMap());
    }
}
