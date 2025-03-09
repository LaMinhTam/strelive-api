package com.skilllease.services;

import com.skilllease.dto.JobCreateDto;
import com.skilllease.entities.Job;
import com.skilllease.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;

public interface JobService {
    @Transactional
    Job createJob(JobCreateDto job);

    Job getJob(Long id) throws EntityNotFoundException;

    List<Job> getAllJobs();

    List<Job> getJobsByEmployer(Long employerId);

    @Transactional
    Job updateJob(Long id, JobCreateDto updatedJob) throws EntityNotFoundException;

    @Transactional
    void deleteJob(Long id);
}
