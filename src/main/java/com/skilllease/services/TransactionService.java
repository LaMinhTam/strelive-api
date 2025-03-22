package com.skilllease.services;

import com.skilllease.entities.Transaction;

import java.util.Optional;

public interface TransactionService {
    Transaction saveTransaction(Transaction transaction);

    Optional<Transaction> findByReference(String reference);
}
