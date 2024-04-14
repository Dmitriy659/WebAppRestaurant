package org.dmitriy.Restaurant.controllers;

import lombok.RequiredArgsConstructor;
import org.dmitriy.Restaurant.models.Product;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/reserve")
    public String reservse_stage1(Principal principal, Model model) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        return "reservation";
    }

    @GetMapping("/reserve_stage2")
    public String reservse_stage2(Principal principal, Model model, @RequestParam int tableId) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);

        List<LocalDateTime> freetime = reservationService.freeTime(tableId);
        List<String> freeTimeString = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (LocalDateTime i : freetime) {
            freeTimeString.add(i.format(formatter));
        }
        model.addAttribute("freetime", freeTimeString);
        System.out.println(freeTimeString);

        model.addAttribute("tableId", tableId);
        return "reservationStage2";
    }

    @PostMapping("/reserve")
    public String reserveTable(Principal principal, @RequestParam int tableId, @RequestParam String date) {
        User user = userService.getUserByPrincipal(principal);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        reservationService.addReservation(user.getId(), tableId, LocalDateTime.parse(date, formatter));
        return "redirect:/profile/" + user.getId();
    }

    @PostMapping("/reserve/delete")
    public String deleteReservation(Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        reservationService.deleteReservation(user.getId());
        return "redirect:/profile/" + user.getId();
    }

    //    http://localhost:8080/admin?login=admin&password=admin
    @GetMapping("/admin")
    public String admin(@RequestParam(required = false) String login, @RequestParam(required = false) String password,
                        Model model) {
        if (login != null && password != null) {
            if (login.equals(admin_login) && password.equals(admin_password)) {
                model.addAttribute("products", productService.allProducts("Все"));
                model.addAttribute("users", userService.allUsers());
                model.addAttribute("pass", admin_password);
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
