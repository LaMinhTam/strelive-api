package com.skilllease.services.impl;

import com.skilllease.dao.MilestoneRepository;
import com.skilllease.dto.FulfillMilestoneDto;
import com.skilllease.dto.FulfillMilestoneFileForm;
import com.skilllease.dto.MilestoneReviewDto;
import com.skilllease.dto.UpdateMilestoneDto;
import com.skilllease.entities.Contract;
import com.skilllease.entities.Milestone;
import com.skilllease.entities.MilestoneStatus;
import com.skilllease.entities.MilestoneSubmissionType;
import com.skilllease.exception.AppException;
import com.skilllease.exception.ErrorCode;
import com.skilllease.services.ContractService;
import com.skilllease.services.MilestoneService;
import com.skilllease.utils.AuthService;
import com.skilllease.utils.CloudinaryUtil;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.List;

@Stateless
public class MilestoneServiceImpl implements MilestoneService {
    @Inject
    private MilestoneRepository milestoneRepository;
    @Inject
    private ContractService contractService;
    @Inject
    private AuthService authService;
    @Inject
    private PaymentService paymentService;

    @Override
    public Milestone fulfillMilestone(Long id, FulfillMilestoneDto dto) throws AppException {
        Milestone milestone = milestoneRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MILESTONE_NOT_FOUND));
        // Ensure current user is the freelancer.
//        if (!milestone.getContract().getFreelancer().getId().equals(authService.getCurrentUser().getId())) {
//            throw new AppException(ErrorCode.UNAUTHORIZED);
//        }
        milestone.setDeliverableUrl(dto.deliverableUrl());
        milestone.setFulfillmentComment(dto.fulfillmentComment());
        milestone.setSubmissionType(MilestoneSubmissionType.LINK);
        milestone.setHidden(true);
        milestone.setReviewStatus(MilestoneStatus.PENDING);
        milestone.setChecklist(dto.checklist());
        return milestoneRepository.update(milestone);
    }

    @Override
    public Milestone fulfillMilestoneWithFile(Long id, FulfillMilestoneFileForm form) throws IOException, AppException {
        Milestone milestone = milestoneRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MILESTONE_NOT_FOUND));
        // Ensure the current user is the freelancer.
//        if (!milestone.getContract().getFreelancer().getId().equals(authService.getCurrentUser().getId())) {
//            throw new AppException(ErrorCode.UNAUTHORIZED);
//        }
        // Upload the file and get the URL.
        String fileUrl = CloudinaryUtil.uploadFile(form.getFile(), form.getFileName());
        milestone.setDeliverableUrl(fileUrl);
        milestone.setFulfillmentComment(form.getFulfillmentComment());
        milestone.setReviewStatus(MilestoneStatus.PENDING);
        milestone.setChecklist(form.getChecklist());
        return milestoneRepository.update(milestone);
    }

    @Override
    public Milestone updateMilestone(Long id, UpdateMilestoneDto dto) throws AppException {
        Milestone milestone = milestoneRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MILESTONE_NOT_FOUND));
        // Ensure current user is the employer.
//        if (!milestone.getContract().getEmployer().getId().equals(authService.getCurrentUser().getId()) && !milestone.getContract().getFreelancer().getId().equals(authService.getCurrentUser().getId())) {
//            throw new AppException(ErrorCode.UNAUTHORIZED);
//        }
        milestone.setTitle(dto.title());
        milestone.setDescription(dto.description());
        milestone.setDueDate(dto.dueDate());
        milestone.setChecklist(dto.checklist());
        return milestoneRepository.update(milestone);
    }

    @Override
    public void deleteMilestone(Long id) throws AppException {
        Milestone milestone = milestoneRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MILESTONE_NOT_FOUND));
        if (!milestone.getJob().getEmployer().getId().equals(authService.getCurrentUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if (!milestone.getReviewStatus().equals(MilestoneStatus.IN_PROGRESS)) {
            throw new AppException(ErrorCode.MILESTONE_IN_PROGRESS);
        }
        milestoneRepository.delete(milestone);
    }

    @Override
    public List<Milestone> findMilestonesByJobId(Long jobId) {
        return milestoneRepository.findByJobId(jobId);
    }

    @Override
    public void saveAll(List<Milestone> milestones) {
        for (Milestone milestone : milestones) {
            milestoneRepository.save(milestone);
        }
    }

    @Override
    public List<Milestone> findMilestonesByContractId(Long contractId) throws AppException {
        Contract contract = contractService.getContractById(contractId);
        List<Milestone> milestones = milestoneRepository.findByJobId(contract.getJobBid().getJob().getId());
        return milestones;
    }

    @Override
    @Transactional
    public void updateAll(List<Milestone> milestones) {
        for (Milestone milestone : milestones)
            if (milestone.getId() != null) {
                milestoneRepository.update(milestone);
            } else {
                // This is a new milestone, create it
                milestoneRepository.save(milestone);
            }
    }

    @Override
    public Milestone reviewMilestone(Long id, MilestoneReviewDto dto) throws AppException {
        Milestone milestone = milestoneRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Milestone not found"));
        if (!milestone.getJob().getEmployer().getId().equals(authService.getCurrentUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        milestone.setReviewStatus(dto.reviewStatus());
        milestone.setFeedback(dto.feedback());
        if (dto.reviewStatus().equals(MilestoneStatus.APPROVED)) {
            paymentService.transferPayment(milestone);
        }
        if (dto.reviewStatus().equals(MilestoneStatus.REJECTED)) {
            paymentService.refundPayment(milestone);
        }
        return milestoneRepository.update(milestone);
    }

    @Override
    public Milestone getMilestoneById(Long id) throws AppException {
        return milestoneRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MILESTONE_NOT_FOUND));
    }

    @Override
    public Milestone updateMilestone(Milestone milestone) {
        return milestoneRepository.update(milestone);
    }
}
