package com.geekbrains.septembermarket.controllers;

import com.geekbrains.septembermarket.entities.Product;
import com.geekbrains.septembermarket.services.ProductsService;
import com.geekbrains.septembermarket.utils.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    private ProductsService productsService;

    private Cart cart;

    @Autowired
    public void setProductsService(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Autowired
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @GetMapping("")
    public String show(Model model, HttpSession httpSession) {
        model.addAttribute("items", cart.getItems().values());
        List list = Collections.list(httpSession.getAttributeNames());
        for (int i = 0; i < list.size(); i++) {
            System.out.println(httpSession.getAttribute("scopedTarget.cart"));
        }
        for(Field f : httpSession.getAttribute("scopedTarget.cart").getClass().getDeclaredFields()) {
            System.out.println(f.getName());
        }
        return "cart";
    }

    @GetMapping("/add")
    public void addProductToCart(@RequestParam("id") Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Product product = productsService.findById(id);
        cart.addProduct(product);
        response.sendRedirect(request.getHeader("referer"));
    }
}
