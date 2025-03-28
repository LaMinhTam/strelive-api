package com.skilllease.services;

import com.skilllease.dto.ServiceCreateDto;
import com.skilllease.entities.Service;
import com.skilllease.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface ServiceService {
    Service createService(Long freelancerId, ServiceCreateDto dto) throws EntityNotFoundException;

    List<Service> getServiceByUserId(Long id);

    Service getServiceById(Long serviceId) throws EntityNotFoundException;
}
