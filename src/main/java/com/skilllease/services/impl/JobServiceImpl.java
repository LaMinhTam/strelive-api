package com.skilllease.services.impl;

import com.skilllease.dao.JobRepository;
import com.skilllease.dto.JobCreateDto;
import com.skilllease.dto.JobDetailDTO;
import com.skilllease.entities.Category;
import com.skilllease.entities.Job;
import com.skilllease.entities.JobBid;
import com.skilllease.exception.AppException;
import com.skilllease.exception.EntityNotFoundException;
import com.skilllease.exception.ErrorCode;
import com.skilllease.mapper.JobMapper;
import com.skilllease.services.ContractService;
import com.skilllease.services.JobBidService;
import com.skilllease.services.JobService;
import com.skilllease.utils.AuthService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class JobServiceImpl implements JobService {
    @Inject
    private JobRepository jobRepository;
    @Inject
    private JobBidService jobBidService;
    @Inject
    private ContractService contractService;
    @Inject
    private AuthService authService;

    @Override
    @Transactional
    public Job createJob(JobCreateDto jobCreateDto) {
        Job job = JobMapper.INSTANCE.toEntity(jobCreateDto);
        job.setCategory(Category.builder().id(jobCreateDto.getCategoryId()).build());
        job.setEmployer(authService.getCurrentUser());
        job.setCreatedAt(LocalDateTime.now());
        return jobRepository.save(job);
    }

    @Override
    public JobDetailDTO getJob(Long id) throws AppException {
        Job job = jobRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
        List<JobBid> bids = jobBidService.getJobBidsByJob(id);
        Long contractId = null;
        for(JobBid bid : bids) {
            if (bid.getStatus().equals("accepted")) {
                contractId = bid.getContract().getId();
            }
        }

        return JobDetailDTO.builder()
                .job(job)
                .bids(bids)
                .contract(contractId != null ? contractService.getContractById(contractId) : null)
                .build();
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll().toList();
    }

    @Override
    public List<Job> getJobsByEmployer(Long employerId) {
        return jobRepository.findByEmployerId(employerId);
    }

    @Override
    @Transactional
    public Job updateJob(Long id, JobCreateDto updatedJob) throws EntityNotFoundException {
        Job existing = jobRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
        existing.setCategory(Category.builder().id(updatedJob.getCategoryId()).build());
        existing.setJobTitle(updatedJob.getJobTitle());
        existing.setJobDescription(updatedJob.getJobDescription());
        existing.setBudget(updatedJob.getBudget());
        existing.setDeadline(updatedJob.getDeadline());
        return jobRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
}
