package com.skilllease.services;

import com.skilllease.dto.CvUploadForm;
import com.skilllease.dto.ServiceDto;
import com.skilllease.entities.Service;
import com.skilllease.entities.User;

import java.io.IOException;
import java.util.List;

public interface FreelancerService {
    User findById(Long id);

    User uploadCv(Long id, CvUploadForm form) throws IOException;

    List<Service> getServicesByFreelancer(Long id);

    Service addService(Long id, ServiceDto service);
}
