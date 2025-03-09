package com.skilllease.services.impl;

import com.skilllease.dao.JobRepository;
import com.skilllease.dto.JobCreateDto;
import com.skilllease.entities.Job;
import com.skilllease.exception.EntityNotFoundException;
import com.skilllease.exception.ErrorCode;
import com.skilllease.mapper.JobMapper;
import com.skilllease.services.JobService;
import com.skilllease.utils.AuthService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@Stateless
public class JobServiceImpl implements JobService {
    @Inject
    private JobRepository jobRepository;
    @Inject
    private AuthService authService;

    @Override
    @Transactional
    public Job createJob(JobCreateDto jobCreateDto) {
        Job job = JobMapper.INSTANCE.toEntity(jobCreateDto);
        job.setEmployer(authService.getCurrentEmployer());
        job.setCreatedAt(java.time.LocalDateTime.now());
        return jobRepository.save(job);
    }

    @Override
    public Job getJob(Long id) throws EntityNotFoundException {
        return jobRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
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
