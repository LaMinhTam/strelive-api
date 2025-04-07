package com.skilllease.services.impl;

import com.skilllease.dao.MilestoneRepository;
import com.skilllease.dto.*;
import com.skilllease.entities.Contract;
import com.skilllease.entities.Milestone;
import com.skilllease.entities.MilestoneStatus;
import com.skilllease.exception.AppException;
import com.skilllease.exception.ErrorCode;
import com.skilllease.services.ContractService;
import com.skilllease.services.MilestoneService;
import com.skilllease.utils.AuthService;
import com.skilllease.utils.CloudinaryUtil;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public List<Milestone> findMilestonesByContract(Long contractId) {
        List<Milestone> milestones = milestoneRepository.findMilestonesByContract(contractId);
        for (Milestone milestone : milestones) {
            if (Boolean.TRUE.equals(milestone.getHidden())) {
                milestone.setDeliverableUrl(null);
            }
        }
        return milestones;
    }

    @Override
    public Milestone createMilestoneInstruction(CreateMilestoneInstructionDto dto) throws AppException {
        Contract contract = contractService.getContractById(dto.contractId());
        // Ensure current user is the employer.
        if (!contract.getEmployer().getId().equals(authService.getCurrentUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        Milestone milestone = Milestone.builder().contract(contract).title(dto.title()).description(dto.description()).submissionType(dto.submissionType()).reviewStatus(MilestoneStatus.IN_PROGRESS).finalMilestone(dto.isFinal()).hidden(dto.isFinal()).dueDate(dto.dueDate()).createdAt(LocalDateTime.now()).checklist(dto.checklist()).build();
        return milestoneRepository.save(milestone);
    }

    @Override
    public Milestone fulfillMilestone(Long id, FulfillMilestoneDto dto) throws AppException {
        Milestone milestone = milestoneRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MILESTONE_NOT_FOUND));
        // Ensure current user is the freelancer.
        if (!milestone.getContract().getFreelancer().getId().equals(authService.getCurrentUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        milestone.setDeliverableUrl(dto.deliverableUrl());
        milestone.setFulfillmentComment(dto.fulfillmentComment());
        milestone.setReviewStatus(MilestoneStatus.PENDING);
        milestone.setUpdatedAt(LocalDateTime.now());
        milestone.setChecklist(dto.checklist());
        return milestoneRepository.update(milestone);
    }

    @Override
    public Milestone fulfillMilestoneWithFile(Long id, FulfillMilestoneFileForm form) throws IOException, AppException {
        Milestone milestone = milestoneRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MILESTONE_NOT_FOUND));
        // Ensure the current user is the freelancer.
        if (!milestone.getContract().getFreelancer().getId().equals(authService.getCurrentUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        // Upload the file and get the URL.
        String fileUrl = CloudinaryUtil.uploadFile(form.getFile(), form.getFileName());
        milestone.setDeliverableUrl(fileUrl);
        milestone.setFulfillmentComment(form.getFulfillmentComment());
        milestone.setUpdatedAt(LocalDateTime.now());
        milestone.setReviewStatus(MilestoneStatus.PENDING);
        milestone.setChecklist(form.getChecklist());
        return milestoneRepository.update(milestone);
    }

    @Override
    public Milestone updateMilestone(Long id, UpdateMilestoneDto dto) throws AppException {
        Milestone milestone = milestoneRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MILESTONE_NOT_FOUND));
        // Ensure current user is the employer.
        if (!milestone.getContract().getEmployer().getId().equals(authService.getCurrentUser().getId()) && !milestone.getContract().getFreelancer().getId().equals(authService.getCurrentUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        milestone.setTitle(dto.title());
        milestone.setDescription(dto.description());
        milestone.setDueDate(dto.dueDate());
        milestone.setChecklist(dto.checklist());
        return milestoneRepository.update(milestone);
    }

    @Override
    public void deleteMilestone(Long id) throws AppException {
        Milestone milestone = milestoneRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MILESTONE_NOT_FOUND));
        if (!milestone.getContract().getEmployer().getId().equals(authService.getCurrentUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if(!milestone.getReviewStatus().equals(MilestoneStatus.IN_PROGRESS)) {
            throw new AppException(ErrorCode.MILESTONE_IN_PROGRESS);
        }
        milestoneRepository.delete(milestone);
    }

    @Override
    public Milestone reviewMilestone(Long id, MilestoneReviewDto dto) throws AppException {
        Milestone milestone = milestoneRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Milestone not found"));
        if (!milestone.getContract().getEmployer().getId().equals(authService.getCurrentUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        milestone.setReviewStatus(dto.reviewStatus());
        if (Boolean.TRUE.equals(milestone.getFinalMilestone())) {
            if (dto.reviewStatus().equals(MilestoneStatus.APPROVED)) {
                paymentService.transferFinalPayment(milestone);
            } else if (dto.reviewStatus().equals(MilestoneStatus.REJECTED)) milestone.setFinalMilestone(false);
        }
        milestone.setFeedback(dto.feedback());
        milestone.setUpdatedAt(LocalDateTime.now());
        return milestoneRepository.update(milestone);
    }

    @Override
    public Milestone getMilestoneById(Long id) throws AppException {
        return milestoneRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MILESTONE_NOT_FOUND));
    }

    @Override
    public Optional<Milestone> findFinalMilestoneByContract(Long id) {
        return milestoneRepository.findFinalMilestoneByContract(id);
    }

    @Override
    public Milestone updateMilestone(Milestone milestone) {
        return milestoneRepository.update(milestone);
    }
}
