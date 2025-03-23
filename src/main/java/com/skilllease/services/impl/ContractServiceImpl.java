package com.skilllease.services.impl;

import com.skilllease.dao.ContractRepository;
import com.skilllease.dto.ContractAcceptDto;
import com.skilllease.dto.CreateContractDto;
import com.skilllease.entities.*;
import com.skilllease.exception.AppException;
import com.skilllease.exception.EntityNotFoundException;
import com.skilllease.exception.ErrorCode;
import com.skilllease.services.ContractService;
import com.skilllease.services.JobBidService;
import com.skilllease.services.MilestoneService;
import com.skilllease.services.ServiceService;
import com.skilllease.utils.AuthService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class ContractServiceImpl implements ContractService {
    @Inject
    private ContractRepository contractRepository;
    @Inject
    private JobBidService jobBidService;
    @Inject
    private ServiceService serviceService;
    @Inject
    private AuthService authService;
    @Inject
    private MilestoneService milestoneService;

    @Transactional
    @Override
    public Contract createContract(CreateContractDto dto) throws AppException {
        // Only an employer can create a contract.
        User currentUser = authService.getCurrentUser();
        Contract contract = new Contract();
        contract.setEmployer(currentUser);
        // The freelancer, service, or job bid would typically be set after selection,
        // but you can let the employer pre-fill if desired.
        contract.setContractType(dto.getContractType());
        if (dto.getContractType().equals(ContractType.BID)) {
            JobBid jobBid = jobBidService.getJobBidById(dto.getJobBidId())
                    .orElseThrow(() -> new EntityNotFoundException(ErrorCode.JOB_BID_NOT_FOUND));
            contract.setJobBid(jobBid);
            contract.setFreelancer(jobBid.getFreelancer());
        } else if (dto.getContractType().equals(ContractType.DIRECT)) {
            Service service = serviceService.getServiceById(dto.getServiceId());
            contract.setService(service);
            contract.setFreelancer(service.getFreelancer());
        }
        contract.setContractStartDate(dto.getContractStartDate());
        contract.setContractEndDate(dto.getContractEndDate());
        contract.setCommitmentPeriod(dto.getCommitmentPeriod());
        contract.setSupportAvailability(dto.getSupportAvailability());
        // Here is where the employer inputs additional policy:
        contract.setAdditionalPolicy(dto.getAdditionalPolicy());
        contract.setDepositAmount(dto.getDepositAmount());
        contract.setFinalPaymentAmount(dto.getFinalPaymentAmount());
        contract.setCreatedAt(LocalDateTime.now());
        // Set initial acceptance flags and status.
        contract.setEmployerAccepted(false);
        contract.setFreelancerAccepted(false);
        contract.setStatus(ContractStatus.DRAFT);
        contract.setDepositStatus(DepositStatus.PENDING);
        contract.setFinalPaymentStatus(PaymentStatus.PENDING);
        jobBidService.updateJobBidStatus(dto.getJobBidId(), "accepted");
        return contractRepository.save(contract);
    }

    @Override
    public Optional<Contract> getContractById(Long id) {
        return contractRepository.findById(id);
    }

    @Transactional
    @Override
    public Contract acceptContract(Long id, ContractAcceptDto acceptDto) throws AppException {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CONTRACT_NOT_FOUND));
        User currentUser = authService.getCurrentUser();
        // Update the acceptance flag only for the current user.
        if (currentUser.getId().equals(contract.getEmployer().getId())) {
            contract.setEmployerAccepted(acceptDto.getEmployerAccepted());
        }
        if (currentUser.getId().equals(contract.getFreelancer().getId())) {
            contract.setFreelancerAccepted(acceptDto.getFreelancerAccepted());
        }
        return contractRepository.save(contract);
    }

    @Transactional
    @Override
    public Contract updateContractStatus(Long id, ContractStatus status) throws AppException {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CONTRACT_NOT_FOUND));
        User currentUser = authService.getCurrentUser();

        // Example check: if freelancer is attempting to cancel unilaterally
        if (status == ContractStatus.CANCELLED) {
            if (currentUser.getId().equals(contract.getFreelancer().getId())) {
                // Check if cancellation by freelancer is allowed under current terms
                // e.g., if employer has not already signaled cancellation or
                // if there's a breach on employer's side.
                // Otherwise, throw an exception or flag for mediation.
            }
            // Similarly, if employer cancels, decide on deposit refund here.
        }

        // Update status if allowed.
        contract.setStatus(status);

        // If the contract is cancelled, trigger deposit handling logic.
        if (status == ContractStatus.CANCELLED) {
            // For instance:
            // if (currentUser is employer) then refund deposit,
            // else if (currentUser is freelancer) then mark deposit as forfeited.
            // This might involve calling additional service methods.
        }

        return contractRepository.save(contract);
    }

    @Transactional
    @Override
    public Contract finalizeContract(Long id) throws AppException {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CONTRACT_NOT_FOUND));

        // Ensure that the current user is the employer.
        if (!authService.getCurrentUser().getId().equals(contract.getEmployer().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Ensure that final payment has been completed.
        if (!contract.getFinalPaymentStatus().equals(PaymentStatus.PAID)) {
            throw new AppException(ErrorCode.PAYMENT_PENDING);
        }

        // Ensure that the final milestone is submitted and approved.
        // milestoneService.findFinalMilestoneByContract() returns Optional<Milestone>
        Milestone finalMilestone = milestoneService.findFinalMilestoneByContract(contract.getId())
                .orElseThrow(() -> new AppException(ErrorCode.FINAL_MILESTONE_NOT_SUBMITTED));
        if (!finalMilestone.getReviewStatus().equals(MilestoneStatus.APPROVED)) {
            throw new AppException(ErrorCode.MILESTONE_NOT_APPROVED);
        }

        // Finalize the contract.
        contract.setStatus(ContractStatus.COMPLETED);
        contract.setContractEndDate(LocalDateTime.now());
        return contractRepository.save(contract);
    }

    @Override
    public Contract save(Contract contract) {
        return contractRepository.save(contract);
    }
}
