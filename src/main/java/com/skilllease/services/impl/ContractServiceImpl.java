package com.skilllease.services.impl;

import com.skilllease.dao.ContractRepository;
import com.skilllease.dto.CreateContractDto;
import com.skilllease.dto.JobDetailDTO;
import com.skilllease.entities.*;
import com.skilllease.exception.AppException;
import com.skilllease.exception.EntityNotFoundException;
import com.skilllease.exception.ErrorCode;
import com.skilllease.services.*;
import com.skilllease.utils.AuthService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ContractServiceImpl implements ContractService {
    @Inject
    private ContractRepository contractRepository;
    @Inject
    private JobService jobService;
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
        if (!currentUser.getRole().equals(Role.EMPLOYER)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        Contract contract = new Contract();
        contract.setEmployer(currentUser);
        // The freelancer, service, or job bid would typically be set after selection,
        // but you can let the employer pre-fill if desired.
        contract.setContractType(dto.getContractType());
        if (dto.getContractType().equals(ContractType.BID)) {
            JobBid jobBid = jobBidService.getJobBidById(dto.getJobBidId());
            contract.setJobBid(jobBid);
            contract.setFreelancer(jobBid.getFreelancer());
        } else if (dto.getContractType().equals(ContractType.DIRECT)) {
            Service service = serviceService.getServiceById(dto.getServiceId());
            contract.setService(service);
            contract.setFreelancer(service.getFreelancer());
        }
        contract.setContractStartDate(dto.getContractStartDate().atStartOfDay());
        contract.setContractEndDate(dto.getContractEndDate().atStartOfDay());
        // Here is where the employer inputs additional policy:
        contract.setAdditionalPolicy(dto.getAdditionalPolicy());
        contract.setCreatedAt(LocalDateTime.now());
        // Set initial acceptance flags and status.
        contract.setStatus(ContractStatus.NEGOTIATION);
        contract.setEmployerAccepted(true);
        jobBidService.updateJobBidStatus(dto.getJobBidId(), "accepted");

        List<Milestone> milestone = milestoneService.findMilestonesByJobId(contract.getJobBid().getJob().getId());
        BigDecimal amount = contract.getJobBid().getBidAmount();
        //base on the effort to update the amount, the effort will be show as percentage
        for (Milestone m : milestone) {
            m.setAmount(amount.multiply(BigDecimal.valueOf(m.getEffort())).divide(BigDecimal.valueOf(100)));
        }
        milestoneService.updateAll(milestone);

        return contractRepository.save(contract);
    }

    @Override
    public Contract getContractById(Long id) throws AppException {
        return contractRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));
    }

    @Transactional
    @Override
    public Contract updateContractStatus(Long id, boolean isAccepted) throws AppException {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CONTRACT_NOT_FOUND));
        User currentUser = authService.getCurrentUser();
        if (currentUser.getId().equals(contract.getEmployer().getId())) {
            contract.setEmployerAccepted(isAccepted);
        } else if (currentUser.getId().equals(contract.getFreelancer().getId())) {
            contract.setFreelancerAccepted(isAccepted);
        }
        if (contract.getEmployerAccepted() != null && contract.getEmployerAccepted() && contract.getFreelancerAccepted() != null && contract.getFreelancerAccepted()) {
            contract.setStatus(ContractStatus.ACTIVE);
        } else {
            contract.setStatus(ContractStatus.NEGOTIATION);
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

    @Override
    public Contract save(Contract contract) {
        return contractRepository.save(contract);
    }

    @Override
    public JobDetailDTO getContractsByJob(Long jobId) throws AppException {
        return jobService.getJob(jobId);
    }

    @Override
    public List<JobDetailDTO> getAllContracts() {
        User currentUser = authService.getCurrentUser();
        List<Contract> contracts = new ArrayList<>();
        if (currentUser.getRole().equals(Role.FREELANCER)) {
            contracts = contractRepository.findByFreelancerId(currentUser.getId()).toList();
        } else if (currentUser.getRole().equals(Role.EMPLOYER)) {
            contracts = contractRepository.findByEmployerId(currentUser.getId()).toList();
        }
        for (Contract contract : contracts) {
            contract.getJobBid().getJob().setMilestones(milestoneService.findMilestonesByJobId(contract.getJobBid().getJob().getId()));
        }
        return contracts.stream().map(contract -> new JobDetailDTO(contract.getJobBid().getJob(), null, contract)).toList();
    }

    @Override
    public Contract getContractByJobId(Long jobId) throws AppException {
        return contractRepository.findByJobId(jobId).orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));
    }
}
