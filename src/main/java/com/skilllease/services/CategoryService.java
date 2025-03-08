package com.skilllease.services;

import com.skilllease.entities.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<Category> findById(Long categoryId);

    Category createCategory(Category category);

    List<Category> getCategories();
}
