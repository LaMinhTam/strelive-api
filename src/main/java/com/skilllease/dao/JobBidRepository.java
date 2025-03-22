package com.skilllease.dao;

import com.skilllease.entities.JobBid;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;
import java.util.List;

@Repository
public interface JobBidRepository extends CrudRepository<JobBid, Long> {
    @Query("SELECT jb FROM JobBid jb WHERE jb.job.id = :jobId")
    List<JobBid> findByJobId(Long jobId);
}
