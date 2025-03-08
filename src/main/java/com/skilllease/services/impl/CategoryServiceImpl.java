package com.skilllease.services.impl;

import com.skilllease.dao.CategoryRepository;
import com.skilllease.entities.Category;
import com.skilllease.services.CategoryService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@Stateless
public class CategoryServiceImpl implements CategoryService {
    @Inject
    private CategoryRepository categoryRepository;

    @Override
    public Optional<Category> findById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll().toList();
    }
}
