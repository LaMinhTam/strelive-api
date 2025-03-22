package com.skilllease.dao;

import com.skilllease.entities.Transaction;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;
import jakarta.data.repository.Save;

import java.util.Optional;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    @Save
    Transaction save(Transaction transaction);

    @Query("SELECT t FROM Transaction t WHERE t.reference = :reference")
    Optional<Transaction> findByReference(String reference);
}
