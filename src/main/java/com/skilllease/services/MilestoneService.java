package com.skilllease.services;

import com.skilllease.dto.CreateMilestoneDto;
import com.skilllease.dto.MilestoneFileForm;
import com.skilllease.dto.MilestoneReviewDto;
import com.skilllease.entities.Milestone;
import com.skilllease.exception.AppException;

import java.io.IOException;

public interface MilestoneService {
    Milestone createMilestone(CreateMilestoneDto dto) throws AppException;
    Milestone createMilestoneWithFile(MilestoneFileForm form) throws IOException, AppException;
    Milestone reviewMilestone(Long id, MilestoneReviewDto dto) throws AppException;
    Milestone getMilestoneById(Long id);
}
