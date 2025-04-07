package com.skilllease.services;

import com.skilllease.dto.CreateContractDto;
import com.skilllease.dto.JobDetailDTO;
import com.skilllease.entities.Contract;
import com.skilllease.entities.ContractStatus;
import com.skilllease.exception.AppException;
import jakarta.transaction.Transactional;

import java.util.List;

public interface ContractService {
    @Transactional
    Contract createContract(CreateContractDto dto) throws AppException;

    Contract getContractById(Long id) throws AppException;

    @Transactional
    Contract updateContractStatus(Long id, boolean isAccepted) throws AppException;

    @Transactional
    Contract updateContractStatus(Long id, ContractStatus status) throws AppException;

    Contract finalizeContract(Long id) throws AppException;

    Contract save(Contract contract);

    JobDetailDTO getContractsByJob(Long jobId) throws AppException;

    List<JobDetailDTO> getAllContracts();
}
