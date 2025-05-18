package org.ticketing_app.contoller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.ticketing_app.model.User;
import org.ticketing_app.user.UserService;

import java.security.Principal;

@Controller
public class WebController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registerUser(user);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            User user = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            model.addAttribute("username", user.getUsername());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("role", user.getRole());
        }
        return "home";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}