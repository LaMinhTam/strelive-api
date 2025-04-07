package com.skilllease.services;

import com.skilllease.dto.CvUploadForm;
import com.skilllease.entities.User;
import com.skilllease.exception.EntityNotFoundException;

import java.io.IOException;

public interface FreelancerService {
    User findById(Long id) throws EntityNotFoundException;

    User uploadCv(Long id, CvUploadForm form) throws IOException, EntityNotFoundException;

}
