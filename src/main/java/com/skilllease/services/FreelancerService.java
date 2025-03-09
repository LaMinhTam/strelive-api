package com.skilllease.services;

import com.skilllease.dto.CvUploadForm;
import com.skilllease.dto.ServiceCreateDto;
import com.skilllease.entities.Service;
import com.skilllease.entities.User;
import com.skilllease.exception.EntityNotFoundException;

import java.io.IOException;
import java.util.List;

public interface FreelancerService {
    User findById(Long id) throws EntityNotFoundException;

    User uploadCv(Long id, CvUploadForm form) throws IOException, EntityNotFoundException;

    List<Service> getServicesByFreelancer(Long id);

    Service addService(Long id, ServiceCreateDto service) throws EntityNotFoundException;
}
