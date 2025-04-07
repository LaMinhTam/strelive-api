package com.skilllease.services;

import com.skilllease.dto.*;
import com.skilllease.entities.Milestone;
import com.skilllease.exception.AppException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface MilestoneService {
    Milestone reviewMilestone(Long id, MilestoneReviewDto dto) throws AppException;

    Milestone getMilestoneById(Long id) throws AppException;

    Optional<Milestone> findFinalMilestoneByContract(Long id);

    Milestone updateMilestone(Milestone milestone);

    List<Milestone> findMilestonesByContract(Long contractId);

    Milestone createMilestoneInstruction(CreateMilestoneInstructionDto dto) throws AppException;

    Milestone fulfillMilestone(Long id, FulfillMilestoneDto dto) throws AppException;

    Milestone fulfillMilestoneWithFile(Long id, FulfillMilestoneFileForm form) throws IOException, AppException;

    Milestone updateMilestone(Long id, UpdateMilestoneDto dto) throws AppException;

    void deleteMilestone(Long id) throws AppException;
}
