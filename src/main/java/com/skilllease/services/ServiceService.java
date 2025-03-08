package com.skilllease.services;

import com.skilllease.dto.ServiceDto;
import com.skilllease.entities.Service;

import java.util.List;

public interface ServiceService {
    Service createService(Long freelancerId, ServiceDto dto);

    List<Service> getServiceByUserId(Long id);
}
