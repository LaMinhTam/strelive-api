package com.skilllease.services.impl;

import com.skilllease.dao.JobBidRepository;
import com.skilllease.dao.JobRepository;
import com.skilllease.dao.UserRepository;
import com.skilllease.dto.JobBidRequestDTO;
import com.skilllease.entities.Job;
import com.skilllease.entities.JobBid;
import com.skilllease.entities.User;
import com.skilllease.exception.AppException;
import com.skilllease.exception.EntityNotFoundException;
import com.skilllease.exception.ErrorCode;
import com.skilllease.services.JobBidService;
import com.skilllease.services.JobService;
import com.skilllease.utils.AuthService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JobBidServiceImpl implements JobBidService {
    @Inject
    private JobService jobService;
    @Inject
    private JobBidRepository jobBidRepository;
    @Inject
    private JobRepository jobRepository;
    @Inject
    private AuthService authService;

    @Transactional
    @Override
    public JobBid createJobBid(JobBidRequestDTO dto) throws AppException {
        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.JOB_NOT_FOUND));
        User freelancer = authService.getCurrentUser();

        JobBid bid = new JobBid();
        bid.setJob(job);
        bid.setFreelancer(freelancer);
        bid.setBidAmount(dto.getBidAmount());
        bid.setCommitmentDays(dto.getCommitmentDays());
        bid.setMessage(dto.getMessage());
        bid.setCreatedAt(LocalDateTime.now());
        bid.setStatus("pending");

        return jobBidRepository.save(bid);
    }

    @Override
    public Optional<JobBid> getJobBidById(Long id) {
        return jobBidRepository.findById(id);
    }

    @Override
    public List<JobBid> getJobBidsByJob(Long jobId) {
        return jobBidRepository.findByJobId(jobId);
    }

    @Transactional
    @Override
    public JobBid updateJobBidStatus(Long bidId, String newStatus) throws AppException {
        JobBid bid = jobBidRepository.findById(bidId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.JOB_BID_NOT_FOUND));

        User employer = authService.getCurrentUser();
        if (!bid.getJob().getEmployer().getId().equals(employer.getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        boolean isAccepted = "accepted".equalsIgnoreCase(newStatus);
        bid.setStatus(isAccepted ? "accepted" : "rejected");

        List<JobBid> bids = jobBidRepository.findByJobId(bid.getJob().getId()).stream()
                .filter(b -> !b.getId().equals(bidId) && !"rejected".equalsIgnoreCase(b.getStatus()))
                .peek(b -> b.setStatus("rejected"))
                .toList();

        jobBidRepository.saveAll(bids);
        return jobBidRepository.save(bid);
    }

    @Transactional
    @Override
    public void deleteJobBid(Long bidId) throws AppException{
        User freelancer =authService.getCurrentUser();
        JobBid bid = jobBidRepository.findById(bidId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.JOB_BID_NOT_FOUND));
        if (!bid.getFreelancer().getId().equals(freelancer.getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        if(!"pending".equalsIgnoreCase(bid.getStatus())){
            throw new AppException(ErrorCode.JOB_BID_CANNOT_BE_DELETED);
        }
        jobBidRepository.deleteById(bidId);
    }
}
