package com.skilllease.dao;

import com.skilllease.entities.Job;
import jakarta.data.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface JobRepository extends CrudRepository<Job, Long> {
    @Query("SELECT j FROM Job j LEFT JOIN FETCH j.milestones WHERE j.employer.id = :employerId")
    List<Job> findByEmployerId(Long employerId);

    @Query("SELECT j FROM Job j JOIN FETCH j.milestones WHERE j.id = :id")
    Optional<Job> findById(Long id);

    @Query("SELECT j FROM Job j LEFT JOIN FETCH j.category JOIN FETCH j.milestones")
    Stream<Job> findAll();

    @Save
    Job save(Job job);
}