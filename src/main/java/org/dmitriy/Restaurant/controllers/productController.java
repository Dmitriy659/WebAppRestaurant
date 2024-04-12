package org.dmitriy.Restaurant.controllers;

import lombok.RequiredArgsConstructor;
import org.dmitriy.Restaurant.models.Product;
import org.dmitriy.Restaurant.services.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// возвращает основыне страницы. Также добавляет и удаляет блюда
@Controller
@RequiredArgsConstructor
public class productController {

    @Value("${admin_page.login}")
    private String admin_login;
    @Value("${admin_page.password}")
    private String admin_password;

    private final ProductService productService;

    @GetMapping("/")
    public String products(Model model) {
        model.addAttribute("products", productService.allProducts());
        return "main_page";
    }

    @PostMapping("/product/create")
    public String createProduct(@RequestParam MultipartFile file, Product product) throws IOException {
        productService.saveProduct(product, file);
        return "redirect:/admin?login=" + admin_login + "&password=" + admin_password;
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin?login=" + admin_login + "&password=" + admin_password;
    }

//    http://localhost:8080/admin?login=admin&password=admin
    @GetMapping("/admin")
    public String admin(@RequestParam(required = false) String login, @RequestParam(required = false) String password,
                        Model model) {
        System.out.println(login + " " + password);
        if (login != null && password != null) {
            if (login.equals(admin_login) && password.equals(admin_password)) {
                model.addAttribute("products", productService.allProducts());
                return "admin_page";
            }
        }
        model.addAttribute("products", productService.allProducts());
        return "main_page";
    }
}
