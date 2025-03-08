package com.skilllease.services;

import com.skilllease.dto.ServiceDto;
import com.skilllease.entities.Service;
import com.skilllease.exception.EntityNotFoundException;

import java.util.List;

public interface ServiceService {
    Service createService(Long freelancerId, ServiceDto dto) throws EntityNotFoundException;

    List<Service> getServiceByUserId(Long id);
}
