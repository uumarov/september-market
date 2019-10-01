package com.geekbrains.septembermarket.controllers;

import com.geekbrains.septembermarket.entities.User;
import com.geekbrains.septembermarket.services.OrderService;
import com.geekbrains.septembermarket.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/create")
    public String createOrder(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        orderService.createOrder(user);
        return "redirect:/shop";
    }

    @GetMapping("/fastcreate")
    public String fastCreateOrder(Model model) {
        if (!orderService.getCart().getItems().isEmpty() && SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            String[] loginPass = userService.fastCreateUser();

            User user = userService.findByUsername(loginPass[0]);
            orderService.createOrder(user);

            model.addAttribute("login", loginPass[0]);
            model.addAttribute("pass", loginPass[1]);
            return "fast-create";
        } else return "redirect:/shop";
    }


}
