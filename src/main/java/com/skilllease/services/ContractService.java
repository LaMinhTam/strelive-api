package com.skilllease.services;

import com.skilllease.dto.ContractAcceptDto;
import com.skilllease.dto.CreateContractDto;
import com.skilllease.entities.Contract;
import com.skilllease.entities.ContractStatus;
import com.skilllease.exception.AppException;
import jakarta.transaction.Transactional;

import java.util.Optional;

public interface ContractService {
    @Transactional
    Contract createContract(CreateContractDto dto) throws AppException;

    Optional<Contract> getContractById(Integer id);

    Contract acceptContract(Integer id, ContractAcceptDto acceptDto) throws AppException;

    @Transactional
    Contract updateContractStatus(Integer id, ContractStatus status) throws AppException;
}
