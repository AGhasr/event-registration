package org.example.eventregistration.controller;

import jakarta.validation.Valid;
import org.example.eventregistration.model.User;
import org.example.eventregistration.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = encoder;
    }

    /**
     * Shows the user registration form.
     * Accessible to anonymous users.
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Handles registration form submission.
     * Validates user input and saves new user with encoded password.
     * Redirects to login on success.
     */
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute User user, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }

        // Check for duplicate username
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            result.rejectValue("username", "error.user", "Username already exists");
            return "register";
        }

        // Encrypt password before storing
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepo.save(user);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
