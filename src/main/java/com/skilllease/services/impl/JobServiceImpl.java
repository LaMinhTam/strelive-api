package com.skilllease.services.impl;

import com.skilllease.dao.JobRepository;
import com.skilllease.dto.*;
import com.skilllease.entities.Category;
import com.skilllease.entities.Job;
import com.skilllease.entities.JobBid;
import com.skilllease.entities.Milestone;
import com.skilllease.exception.AppException;
import com.skilllease.exception.EntityNotFoundException;
import com.skilllease.exception.ErrorCode;
import com.skilllease.services.ContractService;
import com.skilllease.services.JobBidService;
import com.skilllease.services.JobService;
import com.skilllease.services.MilestoneService;
import com.skilllease.utils.AuthService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
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
    @Inject
    private MilestoneService milestoneService;

    @Override
    @Transactional
    public Job createJob(JobCreateDto jobCreateDto) {
        Job job = new Job();
        job.setJobTitle(jobCreateDto.getJobTitle());
        job.setCategory(Category.builder().id(jobCreateDto.getCategoryId()).build());
        job.setEmployer(authService.getCurrentUser());
        job.setCreatedAt(LocalDateTime.now());
        job.setBudget(jobCreateDto.getBudget());
        job.setJobDescription(jobCreateDto.getJobDescription());
        job.setDeadline(jobCreateDto.getDeadline());

        Job savedJob  = jobRepository.save(job);

        List<Milestone> milestones = jobCreateDto.getMilestones().stream()
                .map(milestoneDto -> Milestone.builder()
                        .description(milestoneDto.getDescription())
                        .job(savedJob)
                        .startDate(milestoneDto.getStartDate())
                        .dueDate(milestoneDto.getDueDate())
                        .checklist(milestoneDto.getChecklist())
                        .title(milestoneDto.getTitle())
                        .effort(milestoneDto.getEffort())
                        //base on the effort (percentage and total of the job will have the amount)
                        .amount(new BigDecimal(milestoneDto.getEffort()).multiply(jobCreateDto.getBudget().divide(new BigDecimal(100))))
                        .build())
                .toList();
        job.setMilestones(milestones);
        milestoneService.saveAll(milestones);
        return savedJob;
    }

    @Override
    public JobDetailDTO getJob(Long id) throws AppException {
        Job job = jobRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
        List<JobBid> bids = jobBidService.getJobBidsByJob(id);
        Long contractId = null;
        for (JobBid bid : bids) {
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
    public List<JobDto> getAllJobs() {
        return jobRepository.findAll().toList().stream()
                .map(job -> JobDto.builder()
                        .id(job.getId())
                        .jobTitle(job.getJobTitle())
                        .jobDescription(job.getJobDescription())
                        .budget(job.getBudget())
                        .deadline(job.getDeadline())
                        .employer(UserDto.builder()
                                .id(job.getEmployer().getId())
                                .fullName(job.getEmployer().getFullName())
                                .email(job.getEmployer().getEmail())
                                .build())
                        .category(CategoryDto.builder()
                                .id(job.getCategory().getId())
                                .name(job.getCategory().getName())
                                .build())
                        .build())
                .toList();
    }

    @Override
    public List<JobDto> getJobsByEmployer(Long employerId) {
        List<Job> jobs = jobRepository.findByEmployerId(employerId);
        return jobs.stream()
                .map(job -> JobDto.builder()
                        .id(job.getId())
                        .jobTitle(job.getJobTitle())
                        .jobDescription(job.getJobDescription())
                        .budget(job.getBudget())
                        .deadline(job.getDeadline())
                        .employer(UserDto.builder()
                                .id(job.getEmployer().getId())
                                .fullName(job.getEmployer().getFullName())
                                .email(job.getEmployer().getEmail())
                                .build())
                        .category(CategoryDto.builder()
                                .id(job.getCategory().getId())
                                .name(job.getCategory().getName())
                                .build())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public JobDto updateJob(Long id, JobCreateDto updatedJob) throws EntityNotFoundException {
        Job existing = jobRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
        existing.setCategory(Category.builder().id(updatedJob.getCategoryId()).build());
        existing.setJobTitle(updatedJob.getJobTitle());
        existing.setJobDescription(updatedJob.getJobDescription());
        existing.setBudget(updatedJob.getBudget());
        existing.setDeadline(updatedJob.getDeadline());
        Job job =  jobRepository.save(existing);
        List<Milestone> milestones = updatedJob.getMilestones().stream()
                .map(milestone -> Milestone.builder()
                        .id(milestone.getId())
                        .description(milestone.getDescription())
                        .job(job)
                        .startDate(milestone.getStartDate())
                        .dueDate(milestone.getDueDate())
                        .checklist(milestone.getChecklist())
                        .title(milestone.getTitle())
                        .effort(milestone.getEffort())
                        .amount(new BigDecimal(milestone.getEffort()).multiply(updatedJob.getBudget().divide(new BigDecimal(100))))
                        .build())
                .toList();
        milestoneService.updateAll(milestones);
        return JobDto.builder()
                .id(job.getId())
                .jobTitle(job.getJobTitle())
                .jobDescription(job.getJobDescription())
                .budget(job.getBudget())
                .deadline(job.getDeadline())
                .milestones(job.getMilestones().stream()
                        .map(milestone -> MilestoneDto.builder()
                                .id(milestone.getId())
                                .description(milestone.getDescription())
                                .startDate(milestone.getStartDate())
                                .dueDate(milestone.getDueDate())
                                .checklist(milestone.getChecklist())
                                .amount(milestone.getAmount())
                                .title(milestone.getTitle())
                                .build())
                        .toList())
                .build();
    }

    @Override
    @Transactional
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
}
