package com.geekbrains.septembermarket.services;

import com.geekbrains.septembermarket.entities.ProductsHistory;
import com.geekbrains.septembermarket.entities.User;
import com.geekbrains.septembermarket.repositories.ProductsHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsHistoryService {
    private ProductsHistoryRepository productsHistoryRepository;

    @Autowired
    public void setProductsHistoryRepository(ProductsHistoryRepository productsHistoryRepository) {
        this.productsHistoryRepository = productsHistoryRepository;
    }

    public List<ProductsHistory> findAllByUser(User user) {
        return (List<ProductsHistory>) productsHistoryRepository.findAllByUser(user);
    }

    public void save(ProductsHistory productsHistory) {
        productsHistoryRepository.save(productsHistory);
    }
}
