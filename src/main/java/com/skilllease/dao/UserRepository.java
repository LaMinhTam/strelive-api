package com.skilllease.dao;

import com.skilllease.entities.User;
import jakarta.data.repository.*;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.services WHERE u.id = :id")
    Optional<User> findById(Long id);

    @Insert
    User save(User user);

    @Update
    User update(User user);
}