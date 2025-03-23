package com.skilllease.dto;

import com.skilllease.entities.MilestoneSubmissionType;

import java.time.LocalDateTime;

public record CreateMilestoneDto(
        Long contractId,
        String title,
        String description,
        LocalDateTime dueDate,
        MilestoneSubmissionType submissionType,
        String deliverableUrl,
        boolean isFinal
) {
}
