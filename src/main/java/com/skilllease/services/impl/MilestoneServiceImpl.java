package com.skilllease.services.impl;

import com.skilllease.dao.MilestoneRepository;
import com.skilllease.dto.CreateMilestoneDto;
import com.skilllease.dto.MilestoneFileForm;
import com.skilllease.dto.MilestoneReviewDto;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public Milestone createMilestone(CreateMilestoneDto dto) throws AppException {
        Contract contract = contractService.getContractById(dto.contractId())
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));
        if (!contract.getFreelancer().getId().equals(authService.getCurrentUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        Milestone milestone = Milestone.builder()
                .contract(contract)
                .title(dto.title())
                .description(dto.description())
                .dueDate(dto.dueDate())
                .submissionType(dto.submissionType())
                .deliverableUrl(dto.deliverableUrl())
                .reviewStatus(MilestoneStatus.PENDING)
                .finalMilestone(dto.isFinal())
                .hidden(dto.isFinal())
                .createdAt(LocalDateTime.now())
                .build();

        return milestoneRepository.save(milestone);
    }

    @Override
    public Milestone createMilestoneWithFile(MilestoneFileForm form) throws IOException, AppException {
        Contract contract = contractService.getContractById(form.getContractId())
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));
        if (!contract.getFreelancer().getId().equals(authService.getCurrentUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        String fileUrl = CloudinaryUtil.uploadFile(form.getFile(), UUID.randomUUID().toString());

        Milestone milestone = Milestone.builder()
                .contract(contract)
                .title(form.getTitle())
                .description(form.getDescription())
                .dueDate(
                        form.getDueDate() != null && !form.getDueDate().isEmpty()
                                ? LocalDateTime.parse(form.getDueDate())
                                : null
                )
                .submissionType(MilestoneSubmissionType.FILE)
                .deliverableUrl(fileUrl)
                .reviewStatus(MilestoneStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .finalMilestone(form.isFinal())
                .hidden(form.isFinal())
                .build();

        return milestoneRepository.save(milestone);
    }

    @Override
    public Milestone reviewMilestone(Long id, MilestoneReviewDto dto) throws AppException {
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Milestone not found"));
        if (!milestone.getContract().getEmployer().getId().equals(authService.getCurrentUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        milestone.setReviewStatus(dto.reviewStatus());
        if (Boolean.TRUE.equals(milestone.getFinalMilestone())) {
            if (dto.reviewStatus().equals(MilestoneStatus.APPROVED)) {
                paymentService.transferFinalPayment(milestone);
            } else if (dto.reviewStatus().equals(MilestoneStatus.REJECTED))
                milestone.setFinalMilestone(false);
        }
        milestone.setFeedback(dto.feedback());
        milestone.setUpdatedAt(LocalDateTime.now());
        return milestoneRepository.update(milestone);
    }

    @Override
    public Milestone getMilestoneById(Long id) throws AppException {
        return milestoneRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MILESTONE_NOT_FOUND));
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
