package org.dmitriy.Restaurant.controllers;

import lombok.RequiredArgsConstructor;
import org.dmitriy.Restaurant.models.Product;
import org.dmitriy.Restaurant.services.ProductService;
import org.dmitriy.Restaurant.services.ReservationService;
import org.dmitriy.Restaurant.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    @Value("${admin_page.login}")
    private String admin_login;
    @Value("${admin_page.password}")
    private String admin_password;

    private final ProductService productService;
    private final UserService userService;
    private final ReservationService reservationService;

    // создать товар
    @PostMapping("/product/create")
    public String createProduct(@RequestParam MultipartFile file, Product product) throws IOException {
        productService.saveProduct(product, file);
        return "redirect:/admin?login=" + admin_login + "&password=" + admin_password;
    }

    // удалить товар
    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin?login=" + admin_login + "&password=" + admin_password;
    }

    // редактировать товар
    @PostMapping("/product/edit/{id}")
    public String editProduct(@PathVariable Long id, @RequestParam MultipartFile file, Product product) throws IOException {
        productService.updateProduct(id, file, product);
        return "redirect:/admin?login=" + admin_login + "&password=" + admin_password;
    }

    // удалить пользователя
    @PostMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin?login=" + admin_login + "&password=" + admin_password;
    }

    // удалить бронь у пользователя
    @PostMapping("/admin/deleteRes/{id}")
    public String deleteRes(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return "redirect:/admin?login=" + admin_login + "&password=" + admin_password;
    }
}
