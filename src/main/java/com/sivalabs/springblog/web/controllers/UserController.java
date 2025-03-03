package com.sivalabs.springblog.web.controllers;

import com.sivalabs.springblog.domain.models.CreateUserCmd;
import com.sivalabs.springblog.domain.models.Role;
import com.sivalabs.springblog.domain.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    String registrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationForm("", "", ""));
        return "users/registration";
    }

    @PostMapping("/registration")
    String registerUser(
            @ModelAttribute("user") @Valid UserRegistrationForm userRegistrationForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/registration";
        }
        var cmd = new CreateUserCmd(
                userRegistrationForm.name(),
                userRegistrationForm.email(),
                userRegistrationForm.password(),
                Role.ROLE_USER);
        userService.createUser(cmd);
        return "redirect:/registration-success";
    }

    @GetMapping("/registration-success")
    String registrationSuccess() {
        return "users/registration-success";
    }

    public record UserRegistrationForm(
            @NotBlank(message = "Name is required") String name,
            @NotBlank(message = "Email is required") @Email(message = "Invalid email address") String email,
            @NotBlank(message = "Password is required") String password) {}
}
