package com.geekbrains.septembermarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.geekbrains.septembermarket"})
public class SeptemberMarketApplication {
    // Идеи:
    // Добавить H2 Database
    // Шифрование данных пользователей в таблице

    // Домашнее задание:
    // Как сделать оформление заказа неатвторизованными пользователями

    public static void main(String[] args) {
        SpringApplication.run(SeptemberMarketApplication.class, args);
    }
}
