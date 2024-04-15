package org.dmitriy.Restaurant.controllers;

import lombok.RequiredArgsConstructor;
import org.dmitriy.Restaurant.models.User;
import org.dmitriy.Restaurant.services.ReservationService;
import org.dmitriy.Restaurant.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final UserService userService;
    private final ReservationService reservationService;

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
}
