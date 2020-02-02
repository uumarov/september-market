package com.geekbrains.septembermarket.repositories;

import com.geekbrains.septembermarket.entities.ProductsHistory;
import com.geekbrains.septembermarket.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsHistoryRepository extends JpaRepository<ProductsHistory, Long> {
    List<ProductsHistory> findAllByUser(User user);
}
