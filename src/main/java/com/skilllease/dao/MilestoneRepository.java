package com.skilllease.dao;

import com.skilllease.entities.Milestone;
import jakarta.data.repository.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface MilestoneRepository extends CrudRepository<Milestone, Long> {
    @Query("SELECT m FROM Milestone m WHERE m.id = :id")
    Optional<Milestone> findById(@Param("id") Long id);

    @Insert
    Milestone save(Milestone milestone);

    @Update
    Milestone update(Milestone milestone);

    @Query("SELECT m FROM Milestone m WHERE m.job.id = :jobId")
    List<Milestone> findByJobId(Long jobId);
}
