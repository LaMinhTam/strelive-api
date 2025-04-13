package com.skilllease.services;

import com.skilllease.dto.*;
import com.skilllease.entities.Milestone;
import com.skilllease.exception.AppException;

import java.io.IOException;
import java.util.List;

public interface MilestoneService {
    void updateAll(List<Milestone> milestones);

    Milestone reviewMilestone(Long id, MilestoneReviewDto dto) throws AppException;

    Milestone getMilestoneById(Long id) throws AppException;

    Milestone updateMilestone(Milestone milestone);

    Milestone fulfillMilestone(Long id, FulfillMilestoneDto dto) throws AppException;

    Milestone fulfillMilestoneWithFile(Long id, FulfillMilestoneFileForm form) throws IOException, AppException;

    Milestone updateMilestone(Long id, UpdateMilestoneDto dto) throws AppException;

    void deleteMilestone(Long id) throws AppException;

    List<Milestone> findMilestonesByJobId(Long jobId);

    void saveAll(List<Milestone> milestones);

    List<Milestone> findMilestonesByContractId(Long contractId) throws AppException;
}
