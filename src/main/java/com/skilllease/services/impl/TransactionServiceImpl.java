package com.skilllease.services.impl;

import com.skilllease.dao.TransactionRepository;
import com.skilllease.entities.Transaction;
import com.skilllease.services.TransactionService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.Optional;

@Stateless
public class TransactionServiceImpl implements TransactionService {
    @Inject
    private TransactionRepository transactionRepository;

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> findByReference(String reference) {
        return transactionRepository.findByReference(reference);
    }
}
