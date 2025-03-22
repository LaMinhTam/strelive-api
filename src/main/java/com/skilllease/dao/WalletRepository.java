package com.skilllease.dao;

import com.skilllease.entities.Wallet;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;
import jakarta.data.repository.Save;

import java.util.Optional;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, Long> {
    @Query("SELECT w FROM Wallet w WHERE w.user.id = :id")
    Optional<Wallet> findByUserId(Long id);

    @Save
    Wallet save(Wallet wallet);
}
