package com.skilllease.services;

import com.skilllease.dto.JobBidRequestDTO;
import com.skilllease.entities.JobBid;
import com.skilllease.exception.AppException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface JobBidService {
    @Transactional
    JobBid createJobBid(JobBidRequestDTO dto) throws AppException;

    Optional<JobBid> getJobBidById(Long id);

    List<JobBid> getJobBidsByJob(Long jobId);

    @Transactional
    JobBid updateJobBidStatus(Long bidId, String newStatus) throws AppException;

    @Transactional
    void deleteJobBid(Long bidId) throws AppException;
}
