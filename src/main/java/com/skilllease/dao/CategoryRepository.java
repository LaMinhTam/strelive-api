package com.skilllease.dao;

import com.skilllease.entities.Category;
import jakarta.data.repository.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.id = :id")
    Optional<Category> findById(Long id);

    @Save
    Category save(Category category);
}
