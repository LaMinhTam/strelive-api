package com.skilllease.services.impl;

import com.skilllease.dao.UserRepository;
import com.skilllease.dto.CvUploadForm;
import com.skilllease.entities.Role;
import com.skilllease.entities.User;
import com.skilllease.exception.EntityNotFoundException;
import com.skilllease.exception.ErrorCode;
import com.skilllease.exception.UnauthorizedException;
import com.skilllease.services.FreelancerService;
import com.skilllease.services.ServiceService;
import com.skilllease.utils.AuthService;
import com.skilllease.utils.CloudinaryUtil;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.io.IOException;

@Stateless
public class FreelancerServiceImpl implements FreelancerService {
    @Inject
    private UserRepository userRepository;
    @Inject
    private ServiceService serviceService;
    @Inject
    private AuthService authService;

    @Override
    public User findById(Long id) throws EntityNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
    }

    @Override
    public User uploadCv(Long id, CvUploadForm form) throws IOException, EntityNotFoundException {
        User freelancer = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
        if (!freelancer.getRole().equals(Role.FREELANCER)) {
            throw new UnauthorizedException("Only freelancers can upload CV");
        }
        String cvUrl = CloudinaryUtil.uploadFile(form.getFile(), form.getFileName());
        freelancer.setCvUrl(cvUrl);
        return userRepository.update(freelancer);
    }

}
