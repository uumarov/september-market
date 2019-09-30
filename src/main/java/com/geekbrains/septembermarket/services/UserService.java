package com.geekbrains.septembermarket.services;

import com.geekbrains.septembermarket.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashMap;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);

    void createUser(User user);

    String[] fastCreateUser();
}
