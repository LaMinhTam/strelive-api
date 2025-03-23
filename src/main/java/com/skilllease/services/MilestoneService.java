package com.skilllease.services;

import com.skilllease.dto.CreateMilestoneDto;
import com.skilllease.dto.MilestoneFileForm;
import com.skilllease.dto.MilestoneReviewDto;
import com.skilllease.entities.Milestone;
import com.skilllease.exception.AppException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface MilestoneService {
    Milestone createMilestone(CreateMilestoneDto dto) throws AppException;

    Milestone createMilestoneWithFile(MilestoneFileForm form) throws IOException, AppException;

    Milestone reviewMilestone(Long id, MilestoneReviewDto dto) throws AppException;

    Milestone getMilestoneById(Long id) throws AppException;

    Optional<Milestone> findFinalMilestoneByContract(Long id);

    Milestone updateMilestone(Milestone milestone);

    List<Milestone> findMilestonesByContract(Long contractId);
}
