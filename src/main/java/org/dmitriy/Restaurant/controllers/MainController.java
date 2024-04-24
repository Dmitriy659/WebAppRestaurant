package org.dmitriy.Restaurant.controllers;

import lombok.RequiredArgsConstructor;
import org.dmitriy.Restaurant.models.User;
import org.dmitriy.Restaurant.services.ProductService;
import org.dmitriy.Restaurant.services.ReservationService;
import org.dmitriy.Restaurant.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.format.DateTimeFormatter;

// возвращает основыне страницы. Также добавляет и удаляет блюда
@Controller
@RequiredArgsConstructor
public class MainController {

    @Value("${admin_page.login}")
    private String admin_login;
    @Value("${admin_page.password}")
    private String admin_password;

    private final ProductService productService;
    private final UserService userService;
    private final ReservationService reservationService;

    @GetMapping("/")
    public String products(@RequestParam(required = false, defaultValue = "Все") String category,
                           Model model, Principal principal) {
        model.addAttribute("products", productService.allProducts(category));
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "main_page";
    }

    @GetMapping("/profile/{id}")
    public String getProfile(@PathVariable Long id, Model model, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        if (id.equals(user.getId())) {
            model.addAttribute("user", user);
            if (user.getReservation() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String date = user.getReservation().getDateOfReserv().format(formatter);
                model.addAttribute("date", date);
            }
            return "profile";
        }
        return "error";  // возврат несуществующей страницы
    }


    @GetMapping("/admin")
    public String admin(@RequestParam(required = false) String login, @RequestParam(required = false) String password,
                        Model model) {
        if (login != null && password != null) {
            if (login.equals(admin_login) && password.equals(admin_password)) {
                model.addAttribute("products", productService.allProducts("Все"));
                model.addAttribute("users", userService.allUsers());
                model.addAttribute("pass", admin_password);
                model.addAttribute("reservs", reservationService.getAll());
                return "admin_page";
            }
        }
        return "error"; // возврат несуществующей страницы
    }

    // сделать админом
    @PostMapping("/admin/make/{id}/{password}")
    private String make_admin(@PathVariable Long id, @PathVariable String password) {
        if (password != null && password.equals(admin_password)) {
            userService.make(id);
        }
        return "redirect:/admin?login=" + admin_login + "&password=" + admin_password;
    }

}
