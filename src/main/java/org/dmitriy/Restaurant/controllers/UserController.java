package org.dmitriy.Restaurant.controllers;

import lombok.RequiredArgsConstructor;
import org.dmitriy.Restaurant.models.User;
import org.dmitriy.Restaurant.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration(@RequestParam(required = false) String email, Model model) {
        if (email != null) {
            model.addAttribute("errorMess", "Пользователь с email: " + email + " уже существует");
        }
        return "registration";
    }


    @PostMapping("/registration")
    public String createUser(User user) {
        if (!userService.createUser(user)) {
            return "redirect:/registration?email=" + user.getEmail();
        }
        return "redirect:/login";
    }

    @PostMapping("/updateUser")
    public String updateUser(User updateUser, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        userService.updateUser(user, updateUser);
        return "redirect:/profile/" + user.getId();
    }
}
