package com.skilllease.services;

import com.skilllease.dto.ServiceCreateDto;
import com.skilllease.entities.Service;
import com.skilllease.exception.EntityNotFoundException;

import java.util.List;

public interface ServiceService {
    Service createService(Long freelancerId, ServiceCreateDto dto) throws EntityNotFoundException;

    List<Service> getServiceByUserId(Long id);

    Service getServiceById(Long serviceId) throws EntityNotFoundException;

    List<Service> getAllServices();

    Service addService(ServiceCreateDto service) throws EntityNotFoundException;

    Service updateService(Long serviceId, ServiceCreateDto service) throws EntityNotFoundException;
}
