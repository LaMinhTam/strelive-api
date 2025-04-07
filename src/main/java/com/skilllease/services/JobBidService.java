package com.skilllease.services;

import com.skilllease.dto.JobBidRequestDTO;
import com.skilllease.entities.JobBid;
import com.skilllease.exception.AppException;
import jakarta.transaction.Transactional;

import java.util.List;

public interface JobBidService {
    @Transactional
    JobBid createJobBid(JobBidRequestDTO dto) throws AppException;

    JobBid getJobBidById(Long id) throws AppException;

    List<JobBid> getJobBidsByJob(Long jobId);

    @Transactional
    JobBid updateJobBidStatus(Long bidId, String newStatus) throws AppException;

    @Transactional
    void deleteJobBid(Long bidId) throws AppException;
}
