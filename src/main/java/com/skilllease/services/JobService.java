package com.skilllease.services;

import com.skilllease.dto.JobCreateDto;
import com.skilllease.dto.JobDetailDTO;
import com.skilllease.dto.JobDto;
import com.skilllease.entities.Job;
import com.skilllease.exception.AppException;
import com.skilllease.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;

public interface JobService {
    @Transactional
    Job createJob(JobCreateDto job);

    JobDetailDTO getJob(Long id) throws AppException;

    List<JobDto> getAllJobs();

    List<JobDto> getJobsByEmployer(Long employerId);

    @Transactional
    JobDto updateJob(Long id, JobCreateDto updatedJob) throws EntityNotFoundException;

    @Transactional
    void deleteJob(Long id);
}
