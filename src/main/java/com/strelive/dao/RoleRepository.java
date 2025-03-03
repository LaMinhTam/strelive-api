package com.strelive.dao;

import com.strelive.entities.*;
import jakarta.data.repository.*;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE r.name = :name")
    Optional<Role> findByName(String name);

    @Query("SELECT r FROM Role r")
    Stream<Role> findAll();

    @Insert
    Role save(Role role);
}