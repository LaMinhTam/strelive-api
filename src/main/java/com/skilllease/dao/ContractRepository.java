package com.skilllease.dao;

import com.skilllease.entities.Contract;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

@Repository
public interface ContractRepository extends CrudRepository<Contract, Integer> {
    // Additional custom queries can be added here if needed.
}
