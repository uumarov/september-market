package com.geekbrains.septembermarket.controllers;

import com.geekbrains.septembermarket.entities.Product;
import com.geekbrains.septembermarket.entities.ProductsHistory;
import com.geekbrains.septembermarket.entities.User;
import com.geekbrains.septembermarket.repositories.ProductsHistoryRepository;
import com.geekbrains.septembermarket.repositories.specifications.ProductSpecifications;
import com.geekbrains.septembermarket.services.ProductsHistoryService;
import com.geekbrains.septembermarket.services.ProductsService;
import com.geekbrains.septembermarket.services.UserService;
import com.geekbrains.septembermarket.utils.ProductsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping("/products")
public class ProductsController {
    private ProductsService productsService;
    private UserService userService;
    private ProductsHistoryService productsHistoryService;

    @Autowired
    public void setProductsService(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setProductsHistoryService(ProductsHistoryService productsHistoryService) {
        this.productsHistoryService = productsHistoryService;
    }

    @GetMapping("/{id}")
    public String showProduct(Principal principal, Model model, @PathVariable Long id) {
        Product product = productsService.findById(id);
        if (principal != null) {
            User user = userService.findByPhone(principal.getName());
            ProductsHistory productsHistory = new ProductsHistory(product, user);
            productsHistoryService.save(productsHistory);
        }

        model.addAttribute("product", product);
        return "product";
    }

    @GetMapping("/edit")
    public String showEditForm(Model model, @RequestParam(name = "id", required = false) Long id) {
        Product product = null;
        if (id != null) {
            product = productsService.findById(id);
        } else {
            product = new Product();
        }
        model.addAttribute("product", product);
        return "edit_product";
    }

    @PostMapping("/edit")
    public String saveModifiedProduct(@ModelAttribute(name = "product") Product product) {
        productsService.save(product);
        return "redirect:/products";
    }
}
