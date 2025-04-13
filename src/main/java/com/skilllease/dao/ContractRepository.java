package com.skilllease.dao;

import com.skilllease.entities.Contract;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ContractRepository extends CrudRepository<Contract, Long> {
    @Query("SELECT c FROM Contract c WHERE c.freelancer.id = ?1")
    Stream<Contract> findByFreelancerId(Long id);
    @Query("SELECT c FROM Contract c WHERE c.employer.id = ?1")
    Stream<Contract> findByEmployerId(Long id);
    @Query("SELECT c FROM Contract c WHERE c.jobBid.job.id = ?1")
    Optional<Contract> findByJobId(Long jobId);
}
