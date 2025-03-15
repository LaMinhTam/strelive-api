package com.skilllease.services;

import com.skilllease.dto.JobBidRequestDTO;
import com.skilllease.entities.JobBid;
import com.skilllease.exception.AppException;
import com.skilllease.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface JobBidService {
    @Transactional
    JobBid createJobBid(JobBidRequestDTO dto) throws AppException;

    Optional<JobBid> getJobBidById(Integer id);

    List<JobBid> getJobBidsByJob(Long jobId);

    @Transactional
    JobBid updateJobBidStatus(Integer bidId, String newStatus) throws AppException;

    @Transactional
    void deleteJobBid(Integer bidId) throws AppException;
}
