package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private UserService userService;
    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @GetMapping("/")
    public String showHome() {
        return "home";
    }

    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model) {
        User theUser = userService.findByEmail(user.getEmail());
        if (theUser != null) {
            boolean matches = encoder.matches(user.getPassword(), theUser.getPassword());
            if (matches) {
                model.addAttribute("name", theUser.getName());
                return "main";
            }
        }
        return "redirect:showMyLoginPage?error";
    }

    @GetMapping("/showMyRegistrationInPage")
    public String showMyRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") User user, Model model) {
        User theUser = userService.findByEmail(user.getEmail());
        if (theUser != null) {
            return "redirect:showMySignInPage?error";
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userService.save(user);

        model.addAttribute("name", user.getName());
        return "main";
    }
}
