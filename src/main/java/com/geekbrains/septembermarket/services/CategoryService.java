package com.geekbrains.septembermarket.services;

import com.geekbrains.septembermarket.entities.Category;
import com.geekbrains.septembermarket.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).get();
    }

    public Category findByTitle(String title) {
        return categoryRepository.findOneByTitle(title);
    }

    public List<Category> findAll() {
        return (List<Category>) categoryRepository.findAll();
    }
}
